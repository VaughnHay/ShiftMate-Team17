package com.example.shiftmateOPSC

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DashboardActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var tasks: List<Task>
    private lateinit var focusButton: Button
    private lateinit var addTaskButton:Button
    private lateinit var clockInButton:Button
    private lateinit var myGoalsButton: Button
    private lateinit var viewTotHrsButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_layout)

        // Initialize tasks (replace with actual task data)
        tasks = getTasks()

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.taskRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize TaskAdapter and set it to RecyclerView
        taskAdapter = TaskAdapter(tasks)
        recyclerView.adapter = taskAdapter

        myGoalsButton = findViewById(R.id.myGoalsButton)
        viewTotHrsButton = findViewById(R.id.viewTotHrsButton)
        focusButton = findViewById(R.id.focusButton)
        addTaskButton = findViewById(R.id.addTaskButton)
        clockInButton = findViewById(R.id.clockInBut)


        // Set click listeners
        myGoalsButton.setOnClickListener{
            val intent =Intent(this@DashboardActivity, MinMaxGoalAct::class.java)
            startActivity(intent)
        }

        viewTotHrsButton.setOnClickListener{
            val intent = Intent(this@DashboardActivity, View::class.java)
            startActivity(intent)
        }
        focusButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, FocusActivity::class.java)
            startActivity(intent)
        }

        addTaskButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, AddTask::class.java)
            startActivity(intent)
        }

        clockInButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ClockInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getTasks(): List<Task> {
        // Dummy tasks for demonstration (replace with actual task data retrieval)
        return listOf(
            Task()

        )
    }
}
