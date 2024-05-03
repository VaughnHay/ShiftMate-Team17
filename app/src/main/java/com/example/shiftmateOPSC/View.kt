package com.example.shiftmateOPSC

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
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
        //totalHoursTextView = findViewById(R.id.totalHoursTextView)
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
                        val categoriesLayout = findViewById<LinearLayout>(R.id.categoriesLayout)
                        categoriesLayout.removeAllViews() // Clear previous views

                        for (data in snapshot.children) {
                            val categoryName = data.child("category").getValue(String::class.java) ?: ""
                            val startTime = data.child("startTime").getValue(String::class.java) ?: ""
                            val endTime = data.child("endTime").getValue(String::class.java) ?: ""

                            if (categoryName.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty()) {
                                val startHour = startTime.substringBefore(":").toInt()
                                val endHour = endTime.substringBefore(":").toInt()
                                val duration = endHour - startHour

                                val formattedText = "Category Name: $categoryName,      Total Hours: $duration"

                                // Create a new TextView programmatically
                                val textView = TextView(this@View)
                                textView.text = formattedText
                                //textView.setTextColor(Color.BLACK) // Set text color if needed
                                textView.textSize = 16f // Set text size if needed

                                // Set layout parameters for the TextView
                                val layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                layoutParams.setMargins(0, 0, 0, 16) // Set margins if needed
                                textView.layoutParams = layoutParams

                                // Add the TextView to the categoriesLayout
                                categoriesLayout.addView(textView)
                            }
                        }
                    }



                    override fun onCancelled(error: DatabaseError) {
                        // Handle database error
                    }
                })
            }
        }
    }
}
