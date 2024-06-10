package com.example.shiftmateOPSC

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var progressBarGoal: ProgressBar
    private lateinit var statusTextView: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_details_layout)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        progressBarGoal = findViewById(R.id.taskDetProgressBarGoal)
        statusTextView = findViewById(R.id.taskDetStatusTextView)

        // Retrieve min and max goal hours from intent
        val minGoalHours = intent.getFloatExtra("MIN_HOUR_GOAL", 0f)
        val maxGoalHours = intent.getFloatExtra("MAX_HOUR_GOAL", 0f)

        // Retrieve time spent from intent and convert to hours
        val timeSpentMinutes = intent.getIntExtra("timeSpent", 0)
        val currentProgressHours = timeSpentMinutes / 60f

        // Update progress bar and status
        updateProgressBar(minGoalHours, maxGoalHours, currentProgressHours)

        // Retrieve and display user progress for the past month
        getUserMonthlyProgress(minGoalHours, maxGoalHours)

        // Set onClickListeners for buttons
        findViewById<Button>(R.id.taskDetBackBut).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        findViewById<Button>(R.id.taskDetFocusBut).setOnClickListener {
            startActivity(Intent(this, FocusActivity::class.java))
        }
    }

    private fun updateProgressBar(minGoalHours: Float, maxGoalHours: Float, currentProgressHours: Float) {
        if (currentProgressHours < minGoalHours) {
            progressBarGoal.progress = ((currentProgressHours / minGoalHours) * 50).toInt()
            progressBarGoal.progressTintList = getColorStateList(android.R.color.holo_red_light)
            statusTextView.text = "Below minimum goal"
        } else if (currentProgressHours > maxGoalHours) {
            progressBarGoal.progress = 100
            progressBarGoal.progressTintList = getColorStateList(android.R.color.holo_red_light)
            statusTextView.text = "Above maximum goal"
        } else {
            progressBarGoal.progress = ((currentProgressHours / maxGoalHours) * 100).toInt()
            progressBarGoal.progressTintList = getColorStateList(android.R.color.holo_green_light)
            statusTextView.text = "Within goal range"
        }
    }


    private fun getUserMonthlyProgress(minGoalHours: Float, maxGoalHours: Float) {
        val userId = mAuth.currentUser?.uid ?: return
        val ref = database.getReference("users/$userId/progress")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalHours = 0f
                for (data in snapshot.children) {
                    val hours = data.getValue(Float::class.java) ?: 0f
                    totalHours += hours
                }
                // Calculate average hours per day for the past month
                val averageDailyHours = totalHours / 30f
                updateProgressBar(minGoalHours, maxGoalHours, averageDailyHours)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
