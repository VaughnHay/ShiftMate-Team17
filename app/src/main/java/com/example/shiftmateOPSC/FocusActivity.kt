package com.example.shiftmateOPSC
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class FocusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.focus_layout)

        val exitButton = findViewById<Button>(R.id.focusExitBut)

        exitButton.setOnClickListener {
            // navigating back to the dashboard
            onBackPressed() // This will navigate back to the previous activity (dashboard)
        }
    }
}

