package com.example.shiftmateOPSC

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private fun isWithinDateRange(date: String, startDate: String, endDate: String): Boolean {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val parsedDate = dateFormat.parse(date)
    val parsedStartDate = dateFormat.parse(startDate)
    val parsedEndDate = dateFormat.parse(endDate)

    return parsedDate in parsedStartDate..parsedEndDate
}

class DashboardActivity : AppCompatActivity() {
    private lateinit var focusButton: Button
    private lateinit var addTaskButton: Button
    private lateinit var clockInButton: Button
    private lateinit var myGoalsButton: Button
    private lateinit var viewTotHrsButton: Button
    private lateinit var leaderBoardButton: Button
    private lateinit var profileButton: Button
    private lateinit var chatButton: Button
    private lateinit var categoriesLayout: LinearLayout
    private lateinit var mAuth: FirebaseAuth
    private lateinit var timeLogRef: DatabaseReference
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private lateinit var filterButton: Button
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val tasksList = mutableListOf<Task>()
    //private val filteredTasksList = mutableListOf<Task>()

    private var startDate: Date? = null
    private var endDate: Date? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_layout)

        mAuth = FirebaseAuth.getInstance()
        timeLogRef = FirebaseDatabase.getInstance().getReference("TimeLog")

        categoriesLayout = findViewById(R.id.linear_layout_tasks)

        myGoalsButton = findViewById(R.id.myGoalsButton)
        viewTotHrsButton = findViewById(R.id.viewTotHrsButton)
        focusButton = findViewById(R.id.focusButton)
        addTaskButton = findViewById(R.id.addTaskButton)
        clockInButton = findViewById(R.id.clockInBut)
        leaderBoardButton = findViewById(R.id.leaderBoardButton)
        profileButton = findViewById(R.id.profileButton)
        chatButton = findViewById(R.id.chatButton)
        startDateButton = findViewById(R.id.startDateButton)
        endDateButton = findViewById(R.id.endDateButton)
        filterButton = findViewById(R.id.filterButton)
        tasksRecyclerView = findViewById(R.id.taskRecyclerView)

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
            val intent = Intent(this@DashboardActivity, AddTask::class.java)
            startActivity(intent)
        }

        clockInButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ClockInActivity::class.java)
            startActivity(intent)
        }

        leaderBoardButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, LeaderBoard::class.java)
            startActivity(intent)
        }

        profileButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, Profile::class.java)
            startActivity(intent)
        }

        chatButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, Chat::class.java)
            startActivity(intent)
        }

        startDate = Calendar.getInstance().time
        endDate = Calendar.getInstance().time

        startDateButton.setOnClickListener {
            showDatePickerDialog(true)
        }

        endDateButton.setOnClickListener {
            showDatePickerDialog(false)
        }

        filterButton.setOnClickListener {
            val startDate = startDateButton.text.toString()
            val endDate = endDateButton.text.toString()
            if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                fetchTasksWithinDateRange(startDate, endDate)
            } else {
                // Notify the user to select start and end dates
                Toast.makeText(
                    this@DashboardActivity,
                    "Please select start and end dates",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        taskAdapter = TaskAdapter(tasksList)
        tasksRecyclerView.adapter = taskAdapter

        fetchUserData()
    }

    private fun fetchUserData() {
        val currentUserID = mAuth.currentUser?.uid
        currentUserID?.let { uid ->
            timeLogRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    categoriesLayout.removeAllViews() // Clear previous views

                    for (data in snapshot.children) {
                        val categoryName = data.child("category").getValue(String::class.java) ?: ""
                        val date = data.child("date").getValue(String::class.java) ?: ""
                        val description = data.child("description").getValue(String::class.java) ?: ""
                        val startTime = data.child("startTime").getValue(String::class.java) ?: ""
                        val endTime = data.child("endTime").getValue(String::class.java) ?: ""
                        val imageUrl = data.child("imageUrl").getValue(String::class.java) ?: ""

                        if (categoryName.isNotEmpty() && date.isNotEmpty() && description.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty()) {
                            val formattedText = """
                            Category: $categoryName
                            Date: $date
                            Description: $description
                            Start Time: $startTime
                            End Time: $endTime
                        """.trimIndent()

                            // Create a new LinearLayout for each entry
                            val layout = LinearLayout(this@DashboardActivity)
                            layout.orientation = LinearLayout.VERTICAL
                            val layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            layoutParams.setMargins(0, 0, 0, 16)
                            layout.layoutParams = layoutParams

                            // Create and add TextView for formatted text
                            val textView = TextView(this@DashboardActivity)
                            textView.text = formattedText
                            textView.textSize = 16f
                            layout.addView(textView)

                            // If there's an image URL, create and add ImageView for image
                            if (imageUrl.isNotEmpty()) {
                                val imageView = ImageView(this@DashboardActivity)
                                Glide.with(this@DashboardActivity)
                                    .load(imageUrl)
                                    .into(imageView)
                                layout.addView(imageView)
                            }
                            // Create and add ImageView for image
                            val imageView = ImageView(this@DashboardActivity)
                            Glide.with(this@DashboardActivity)
                                .load(imageUrl)
                                .into(imageView)
                            layout.addView(imageView)

                            // Add the LinearLayout to categoriesLayout
                            categoriesLayout.addView(layout)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    private fun showDatePickerDialog(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                if (isStartDate) {
                    startDate = selectedDate.time
                    startDateButton.text = formatDate(startDate)
                } else {
                    endDate = selectedDate.time
                    endDateButton.text = formatDate(endDate)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
    private fun fetchTasksWithinDateRange(startDate: String, endDate: String) {
        val currentUserID = mAuth.currentUser?.uid
        currentUserID?.let { uid ->
            timeLogRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    categoriesLayout.removeAllViews() // Clear previous views

                    for (data in snapshot.children) {
                        val categoryName = data.child("category").getValue(String::class.java) ?: ""
                        val date = data.child("date").getValue(String::class.java) ?: ""
                        val description = data.child("description").getValue(String::class.java) ?: ""
                        val startTime = data.child("startTime").getValue(String::class.java) ?: ""
                        val endTime = data.child("endTime").getValue(String::class.java) ?: ""
                        val imageUrl = data.child("imageUrl").getValue(String::class.java) ?: ""

                        // Check if the task falls within the specified date range
                        if (categoryName.isNotEmpty() && date.isNotEmpty() && description.isNotEmpty()
                            && startTime.isNotEmpty() && endTime.isNotEmpty() && imageUrl.isNotEmpty()
                            && isWithinDateRange(date, startDate, endDate)
                        ) {
                            val formattedText =
                                "\n\nCategory: $categoryName\nDate: $date\nDescription: $description\nStart Time: $startTime\nEnd Time: $endTime \n" +
                                        "Category Image:\n"

                            // Create a new LinearLayout for each entry
                            val layout = LinearLayout(this@DashboardActivity)
                            layout.orientation = LinearLayout.VERTICAL
                            val layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            layoutParams.setMargins(0, 0, 0, 16)
                            layout.layoutParams = layoutParams

                            // Create and add TextView for formatted text
                            val textView = TextView(this@DashboardActivity)
                            textView.text = formattedText
                            textView.textSize = 16f
                            layout.addView(textView)

                            // Create and add ImageView for image
                            val imageView = ImageView(this@DashboardActivity)
                            Glide.with(this@DashboardActivity)
                          .load(imageUrl)
                                .into(imageView)
                            layout.addView(imageView)

                            // Add the LinearLayout to categoriesLayout
                            categoriesLayout.addView(layout)
                        }
                    }
                    // Notify the user that filtering was successful
                    Toast.makeText(
                        this@DashboardActivity,
                        "Tasks filtered successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    private fun formatDate(date: Date?): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    }
}

