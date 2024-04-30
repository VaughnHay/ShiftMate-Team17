package com.example.shiftmateOPSC
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ClockInActivity : AppCompatActivity(){

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clockin_layout)

        //Initializing the Firebase database
        database = FirebaseDatabase.getInstance().reference

        val clockInBut: Button = findViewById(R.id.clockInBut)
        val clockInBackBut: Button = findViewById(R.id.clockInBackBut)
        val verifyBut: Button = findViewById(R.id.verifyBut)
        val calendarView: CalendarView = findViewById(R.id.calendarView)
        val clockInDateEditText: EditText = findViewById(R.id.clockInDateEditText)
        val clockInTimeEditText: EditText = findViewById(R.id.clockInTimeEditText)
        val clockInLocEditText: EditText = findViewById(R.id.clockInLocEditText)
        val clockInEmpCodeEditText: EditText = findViewById(R.id.clockInEmpCodeEditText)

        //Listener for the calendarview to update the date field
        calendarView.setOnDateChangeListener{ _,year, month, dayOfMonth ->
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
        verifyBut.setOnClickListener{
            //Implement logic here
            // checks if employee code provided exists and is assigned to the logged-in user
        }

        // Set onClick listener for the clock-in button
        clockInBut.setOnClickListener {
            // Retrieve data from EditText fields
            val date = clockInDateEditText.text.toString()
            val time = clockInTimeEditText.text.toString()
            val location = clockInLocEditText.text.toString()
            val empCode = clockInEmpCodeEditText.text.toString()

            // Save data to Firebase database
            saveClockInData(date, time, location, empCode)

            // Finish the activity or perform any other action
            finish()
        }

        clockInBut.setOnClickListener {
            //navigates back to the dashboard page
            finish()
        }
    }

    private fun saveClockInData(date: String, time: String, location: String, empCode: String){
        //saves data to database
        val clockInData = ClockInData(date, time, location,empCode)
        val clockInId = database.child("clock_ins").push().key
        if(clockInId != null){
            database.child("clock_ins").child(clockInId).setValue(clockInData)
        }
    }
    data class ClockInData(val date: String, val time: String, val location: String, val empCode: String)

}