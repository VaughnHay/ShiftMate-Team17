package com.example.shiftmateOPSC

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var tasks: MutableList<Task> // Change to MutableList<Task>
    private lateinit var focusButton: Button
    private lateinit var addTaskButton: Button
    private lateinit var clockInButton: Button
    private lateinit var myGoalsButton: Button
    private lateinit var viewTotHrsButton: Button
    private lateinit var leaderBoardButton: Button
    private lateinit var profileButton: Button
    private lateinit var chatButton: Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_layout)

        //Initializing the firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("TimeLog").child(firebaseAuth.currentUser!!.uid)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.taskRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize TaskAdapter and set it to RecyclerView
        tasks = mutableListOf() // Initialize tasks list
        taskAdapter = TaskAdapter(tasks)
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
        retrieveTasks()
    }

    private fun retrieveTasks() {
        // Retrieve tasks from Firebase Database
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear existing tasks list
                tasks.clear()

                for (taskSnapshot in dataSnapshot.children) {
                    val task = taskSnapshot.getValue(Task::class.java)
                    task?.let {
                        tasks.add(it)
                    }
                }
                // Notify adapter of data change
                taskAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
