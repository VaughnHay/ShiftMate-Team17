package com.example.shiftmateOPSC

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GraphActivity : AppCompatActivity() {

    private lateinit var customBarChartView: CustomBarChartView
    private lateinit var backButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var goalsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.graph_layout)

        customBarChartView = findViewById(R.id.customBarChartView)
        backButton = findViewById(R.id.BackBTN)
        mAuth = FirebaseAuth.getInstance()
        goalsRef = FirebaseDatabase.getInstance().getReference("Goals")
        backButton.setOnClickListener {
            val intent = Intent(this@GraphActivity, DashboardActivity::class.java)
            startActivity(intent)
        }
        fetchGoalData()
    }

    private fun fetchGoalData() {
        val userId = mAuth.currentUser?.uid ?: return
        goalsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = mutableListOf<GoalData>()
                for (goalSnapshot in snapshot.children) {
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    if (goal != null) {
                        val goalName = goal.goalName ?: ""
                        val minHourGoal = goal.minHourGoal?.toFloatOrNull() ?: 0f
                        val maxHourGoal = goal.maxHourGoal?.toFloatOrNull() ?: 0f
                        data.add(GoalData(goalName, minHourGoal, maxHourGoal))
                    }
                }
                customBarChartView.data = data
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
