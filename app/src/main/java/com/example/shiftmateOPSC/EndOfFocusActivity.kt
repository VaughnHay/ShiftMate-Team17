package com.example.shiftmateOPSC

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EndOfFocusActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.end_of_focus_layout)

        val endMessageTextView = findViewById<TextView>(R.id.endMessageTextView)
        val continueButton = findViewById<Button>(R.id.continueButton)
        val timeSpent = intent.getIntExtra("timeSpent", 0)
        val category = intent.getStringExtra("category")

        val message = "Well done! You focused for $timeSpent minutes on $category."
        endMessageTextView.text = message

        continueButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
