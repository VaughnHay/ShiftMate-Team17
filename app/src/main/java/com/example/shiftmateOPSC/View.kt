package com.example.shiftmateOPSC

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    // Initialize Firebase
    private val database = FirebaseDatabase.getInstance()
    private val taskRef = database.getReference("tasks")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.HtimeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create an empty list to hold the tasks
        val tasks = mutableListOf<Task22>()
        val adapter = TaskAdapter2(tasks)
        recyclerView.adapter = adapter

        // Set up ValueEventListener to listen for changes in Firebase database
        taskRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear the existing tasks
                tasks.clear()

                // Iterate through the dataSnapshot to get the tasks
                for (snapshot in dataSnapshot.children) {
                    val task = snapshot.getValue(Task22::class.java)
                    task?.let {
                        // Calculate total hours and update the task
                        val totalHours = calculateTotalHours(it.startTime, it.endTime)
                        it.totalHours = totalHours
                        // Add the updated task to the list
                        tasks.add(it)
                    }
                }

                // Update the adapter with the new list of tasks
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    // Function to calculate total hours
    private fun calculateTotalHours(startTime: String, endTime: String): Int {
        val startHour = startTime.split(":")[0].toInt()
        val startMinute = startTime.split(":")[1].toInt()

        val endHour = endTime.split(":")[0].toInt()
        val endMinute = endTime.split(":")[1].toInt()

        val totalMinutes = (endHour * 60 + endMinute) - (startHour * 60 + startMinute)
        val totalHours = totalMinutes / 60

        return totalHours
    }

}

