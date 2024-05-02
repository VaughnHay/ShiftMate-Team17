package com.example.shiftmateOPSC

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Chat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_layout)

        //Using type inference for findViewById
        val backButton = findViewById<Button>(R.id.chatBackBut)
        backButton.setOnClickListener {
            // Go back to the previous activity
            finish()
        }
    }
}
