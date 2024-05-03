package com.example.shiftmateOPSC

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class View : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var timeLogRef: DatabaseReference
    private lateinit var totalHoursButton: Button
    private lateinit var totalHoursTextView: TextView
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        mAuth = FirebaseAuth.getInstance()
        timeLogRef = FirebaseDatabase.getInstance().getReference("TimeLog")
        totalHoursButton = findViewById(R.id.totalHoursButton)
        totalHoursTextView = findViewById(R.id.totalHoursTextView)
        backBtn = findViewById(R.id.Back2mainbtn)

        backBtn.setOnClickListener{
            startActivity(Intent(this@View, DashboardActivity::class.java))
            finish()
        }

        totalHoursButton.setOnClickListener {
            val currentUserID = mAuth.currentUser?.uid
            currentUserID?.let { uid ->
                timeLogRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var totalHours = 0
                        for (data in snapshot.children) {
                            val startTime = data.child("startTime").getValue(String::class.java) ?: ""
                            val endTime = data.child("endTime").getValue(String::class.java) ?: ""

                            if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
                                // Parse the start and end times to calculate the duration
                                val startHour = startTime.substringBefore(":").toInt()
                                val endHour = endTime.substringBefore(":").toInt()
                                val duration = endHour - startHour

                                // Add the duration to the total hours
                                totalHours += duration
                            }
                        }
                        totalHoursTextView.text = "Total Hours: $totalHours"
                    }


                    override fun onCancelled(error: DatabaseError) {
                        // Handle database error
                    }
                })
            }
        }
    }
}
