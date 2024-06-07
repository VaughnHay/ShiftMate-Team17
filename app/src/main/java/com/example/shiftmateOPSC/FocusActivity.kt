package com.example.shiftmateOPSC

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FocusActivity : AppCompatActivity() {

    private lateinit var chronometer: Chronometer
    private lateinit var categoryTextView: TextView
    private var startTime: Long = 0
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.focus_layout)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        val exitButton = findViewById<Button>(R.id.focusExitBut)
        chronometer = findViewById(R.id.chronometer)
        categoryTextView = findViewById(R.id.categoryTextView)

        val selectedCategory = intent.getStringExtra("selectedCategory")
        categoryTextView.text = selectedCategory

        startTime = SystemClock.elapsedRealtime()
        chronometer.base = startTime
        chronometer.start()

        exitButton.setOnClickListener {
            chronometer.stop()
            val elapsedMillis = SystemClock.elapsedRealtime() - startTime
            val elapsedMinutes = (elapsedMillis / 1000 / 60).toInt()

            // Save to Firebase
            val currentUser = firebaseAuth.currentUser
            currentUser?.uid?.let { userId ->
                val focusSession = hashMapOf(
                    "category" to selectedCategory,
                    "timeSpent" to elapsedMinutes
                )
                firebaseDatabase.reference.child("FocusSessions").child(userId).push().setValue(focusSession)
            }

            // Navigate to EndOfFocus activity
            val intent = Intent(this, EndOfFocusActivity::class.java)
            intent.putExtra("timeSpent", elapsedMinutes)
            intent.putExtra("category", selectedCategory)
            startActivity(intent)
            finish()
        }
    }
}
