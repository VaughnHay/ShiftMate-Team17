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
    private lateinit var tasks: MutableList<AddTask> // Change to MutableList<AddTask>
    private lateinit var focusButton: Button
    private lateinit var addTaskButton: Button
    private lateinit var clockInButton: Button
    private lateinit var myGoalsButton: Button
    private lateinit var viewTotHrsButton: Button
    private lateinit var leaderBoardButton: Button
    private lateinit var profileButton: Button
    private lateinit var chatButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_layout)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.taskRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize TaskAdapter and set it to RecyclerView
        tasks = mutableListOf() // Initialize tasks list
       // taskAdapter = TaskAdapter(tasks)
        recyclerView.adapter = taskAdapter

        myGoalsButton = findViewById(R.id.myGoalsButton)
        viewTotHrsButton = findViewById(R.id.viewTotHrsButton)
        focusButton = findViewById(R.id.focusButton)
        addTaskButton = findViewById(R.id.addTaskButton)
        clockInButton = findViewById(R.id.clockInBut)
        leaderBoardButton = findViewById(R.id.leaderBoardButton)
        profileButton = findViewById(R.id.profileButton)
        chatButton = findViewById(R.id.chatButton)

        // Set click listeners
        myGoalsButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, MinMaxGoalAct::class.java)
            startActivity(intent)
        }

        focusButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, FocusActivity::class.java)
            startActivity(intent)
        }

        viewTotHrsButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, View::class.java)
            startActivity(intent)
        }

        addTaskButton.setOnClickListener {
            // Start the AddTask activity
            val intent = Intent(this@DashboardActivity,AddTask::class.java)
            startActivity(intent)
        }

        clockInButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ClockInActivity::class.java)
            startActivity(intent)
        }

        leaderBoardButton.setOnClickListener{
            val intent = Intent(this@DashboardActivity, LeaderBoard::class.java)
            startActivity(intent)
        }

        profileButton.setOnClickListener{
            val intent = Intent(this@DashboardActivity,Profile::class.java)
            startActivity(intent)
        }

        chatButton.setOnClickListener{
            val intent = Intent(this@DashboardActivity, Chat::class.java)
            startActivity(intent)
        }
    }

    // Define the request code for adding a task
    /*private val ADD_TASK_REQUEST_CODE = 100

    // Handle the result from AddTaskActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the task data from the intent
            val category = data?.getStringExtra("category")
            val dateOfEntry = data?.getStringExtra("dateOfEntry")

            // Add the task to the list
            if (!category.isNullOrEmpty() && !dateOfEntry.isNullOrEmpty()) {
                tasks.add(AddTask(category, dateOfEntry)) // Use AddTask constructor
                taskAdapter.notifyDataSetChanged() // Notify adapter of data change
            }
        }
    }*/
}


