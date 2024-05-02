package com.example.shiftmateOPSC

import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ClockInActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clockin_layout)

        //Initializing the Firebase database
        database = FirebaseDatabase.getInstance().reference

        //Initializing Firebase Authentication
        auth = FirebaseAuth.getInstance()

        val clockInBut: Button = findViewById(R.id.clockInBut)
        val clockInBackBut: Button = findViewById(R.id.clockInBackBut)
        val verifyBut: Button = findViewById(R.id.verifyBut)
        val calendarView: CalendarView = findViewById(R.id.calendarView)
        val clockInDateEditText: EditText = findViewById(R.id.clockInDateEditText)
        val clockInTimeEditText: EditText = findViewById(R.id.clockInTimeEditText)
        val clockInLocEditText: EditText = findViewById(R.id.clockInLocEditText)
        val clockInEmailEditText: EditText = findViewById(R.id.clockInEmailEditText)

        //Listener for the calendar view to update the date field
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val formattedDate = sdf.format(selectedDate.time)
            clockInDateEditText.setText(formattedDate)
        }

        //Setting the current time to the time field
        val currentTime = Calendar.getInstance().time
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = timeFormat.format(currentTime)
        clockInTimeEditText.setText(formattedTime)

        //Verify button
        verifyBut.setOnClickListener {
            //Implement logic here
            val email = clockInEmailEditText.text.toString()
            val currentUser = auth.currentUser

            if (currentUser != null && email == currentUser.email) {
                // Proceed with further verification or action
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Incorrect email address provided. Please enter the correct email address.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        // Set onClick listener for the clock-in button
        clockInBut.setOnClickListener {
            // Retrieve data from EditText fields
            val date = clockInDateEditText.text.toString()
            val time = clockInTimeEditText.text.toString()
            val location = clockInLocEditText.text.toString()
            val empVerify = clockInEmailEditText.text.toString()

            // Save data to Firebase database
            saveClockInData(date, time, location, empVerify)

            //Successful login
            Snackbar.make(
                findViewById(android.R.id.content),
                "You have successfully clocked in.",
                Snackbar.LENGTH_LONG
            ).show()
            // Finish the activity or perform any other action
            //finish()
        }

        // Set onClick listener for the back button
        clockInBackBut.setOnClickListener {
            // Navigate back to the previous activity (DashboardActivity)
            finish()
        }
    }

    private fun saveClockInData(date: String, time: String, location: String, empVerify: String) {
        //saves data to database
        val clockInData = ClockInData(date, time, location, empVerify)
        val clockInId = database.child("clock_ins").push().key
        if (clockInId != null) {
            database.child("clock_ins").child(clockInId).setValue(clockInData)
        }
    }

    data class ClockInData(val date: String, val time: String, val location: String, val empVerify: String)

}

