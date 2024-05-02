package com.example.shiftmateOPSC

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LeaderBoard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leader_board_layout)

        val backButton: Button = findViewById(R.id.leaderBackBut)
        backButton.setOnClickListener {
            // Go back to the DashboardActivity
            finish()
        }
    }
}
