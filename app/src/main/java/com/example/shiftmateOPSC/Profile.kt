package com.example.shiftmateOPSC

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Profile: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_layout)

        val backButton: Button = findViewById(R.id.profileBackBut)
        backButton.setOnClickListener {
            // Go back to the DashboardActivity
            finish()
        }
    }
}