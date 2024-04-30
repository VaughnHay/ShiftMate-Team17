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

        focusButton = findViewById(R.id.focusButton)

        //Setting the onCLickListener
        focusButton.setOnClickListener{
            val intent = Intent(this@DashboardActivity, FocusActivity::class.java)
            startActivity(intent)

            val addTaskButton: Button = findViewById(R.id.addTaskButton)
            addTaskButton.setOnClickListener{
                val intent = Intent(this, AddTask::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getTasks(): List<Task> {
        // Dummy tasks for demonstration (replace with actual task data retrieval)
        return listOf(
            Task()

        )
    }
}
