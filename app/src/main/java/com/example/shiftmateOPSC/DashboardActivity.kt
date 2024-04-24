package com.example.shiftmateOPSC
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DashboardActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var tasks: List<Task>

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
    }

    private fun getTasks(): List<Task> {
        // Dummy tasks for demonstration (replace with actual task data retrieval)
        return listOf(
            Task()

        )
    }
}
