package com.example.shiftmateOPSC

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MinMaxGoalAct : AppCompatActivity() {

    private lateinit var goalNameEditText: EditText
    private lateinit var minHourGoalEditText: EditText
    private lateinit var maxHourGoalEditText: EditText
    private lateinit var displayGoalTextView: TextView
    private lateinit var submitGoalButton: Button
    private lateinit var displayAllGoalsButton: Button
    private lateinit var displayPreviousButton: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var goalsRef: DatabaseReference
    private val goalsList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_min_max_goal)

        mAuth = FirebaseAuth.getInstance()
        goalsRef = FirebaseDatabase.getInstance().getReference("Goals")

        goalNameEditText = findViewById(R.id.goalName)
        minHourGoalEditText = findViewById(R.id.minHourGoal)
        maxHourGoalEditText = findViewById(R.id.maxHourGoal)
        displayGoalTextView = findViewById(R.id.displayGoal)
        submitGoalButton = findViewById(R.id.submitGoal)
        displayAllGoalsButton = findViewById(R.id.displayAllGoals)
        displayPreviousButton = findViewById(R.id.Backbtn)

        submitGoalButton.setOnClickListener {
            val goalName = goalNameEditText.text.toString()
            val minHourGoal = minHourGoalEditText.text.toString()
            val maxHourGoal = maxHourGoalEditText.text.toString()

            if (goalName.isBlank() && minHourGoal.isBlank() && maxHourGoal.isBlank()) {
                val noGoalsMessage = "No goals added"
                displayGoalTextView.text = noGoalsMessage
                Toast.makeText(this, noGoalsMessage, Toast.LENGTH_SHORT).show()
            } else if (goalName.isBlank() || minHourGoal.isBlank() || maxHourGoal.isBlank()) {
                val incompleteFieldsMessage = "Please fill in all goal details"
                displayGoalTextView.text = incompleteFieldsMessage
                Toast.makeText(this, incompleteFieldsMessage, Toast.LENGTH_SHORT).show()
            } else {
                addGoalToDatabase(goalName, minHourGoal, maxHourGoal)
            }

            goalNameEditText.text.clear()
            minHourGoalEditText.text.clear()
            maxHourGoalEditText.text.clear()
        }

        displayAllGoalsButton.setOnClickListener {
            fetchAllGoals()
        }

        displayPreviousButton.setOnClickListener {
            val intent = Intent(this@MinMaxGoalAct, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addGoalToDatabase(goalName: String, minHourGoal: String, maxHourGoal: String) {
        val userId = mAuth.currentUser?.uid ?: return
        val goalId = goalsRef.push().key ?: return
        val goal = Goal(goalId, userId, goalName, minHourGoal, maxHourGoal)

        goalsRef.child(goalId).setValue(goal)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val goalInfo = """
                        Name: $goalName
                        Min Hourly Goal: $minHourGoal
                        Max Hourly Goal: $maxHourGoal
                    """.trimIndent()
                    goalsList.add(goalInfo)
                    displayGoalTextView.text = getString(R.string.added_goal_template, goalName, minHourGoal, maxHourGoal)
                    Toast.makeText(this, "New goal added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to add goal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun fetchAllGoals() {
        val userId = mAuth.currentUser?.uid ?: return
        goalsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                goalsList.clear()
                for (goalSnapshot in snapshot.children) {
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    if (goal != null) {
                        val goalInfo = """
                            Name: ${goal.goalName}
                            Min Hourly Goal: ${goal.minHourGoal}
                            Max Hourly Goal: ${goal.maxHourGoal}
                        """.trimIndent()
                        goalsList.add(goalInfo)
                    }
                }
                val allGoalsText = goalsList.joinToString("\n\n")
                displayGoalTextView.text = allGoalsText.ifEmpty { getString(R.string.no_goals_added) }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MinMaxGoalAct, "Failed to fetch goals: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
