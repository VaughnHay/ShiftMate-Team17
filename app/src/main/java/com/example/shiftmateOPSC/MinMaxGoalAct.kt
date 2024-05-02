package com.example.shiftmateOPSC

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class MinMaxGoalAct : AppCompatActivity() {

    private lateinit var goalNameEditText: EditText
    private lateinit var minHourGoalEditText: EditText
    private lateinit var maxHourGoalEditText: EditText
    private lateinit var displayGoalTextView: TextView
    private lateinit var submitGoalButton: Button
    private lateinit var displayAllGoalsButton: Button
    private lateinit var displayPreviousButton: Button

    private val goalsList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_min_max_goal)

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

                Toast.makeText(this, incompleteFieldsMessage, Toast.LENGTH_SHORT).show()} else {
                val goalInfo = """
            Name: $goalName
            Min Hourly Goal: $minHourGoal
            Max Hourly Goal: $maxHourGoal
        """.trimIndent()
                goalsList.add(goalInfo)
                displayGoalTextView.text = getString(R.string.added_goal_template, goalName, minHourGoal, maxHourGoal)
                Toast.makeText(this, "New goal added!", Toast.LENGTH_SHORT).show()
            }

            goalNameEditText.text.clear()
            minHourGoalEditText.text.clear()
            maxHourGoalEditText.text.clear()
        }

        displayAllGoalsButton.setOnClickListener {
            val allGoalsText = goalsList.joinToString("\n\n")
            displayGoalTextView.text = allGoalsText.ifEmpty { getString(R.string.no_goals_added) }

        }
        displayPreviousButton.setOnClickListener{
            val intent = Intent(this@MinMaxGoalAct, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}