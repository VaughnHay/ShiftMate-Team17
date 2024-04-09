package com.example.shiftmateOPSC

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
class LoginActivity: AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerTextView = findViewById(R.id.registerTextView)

        // Set click listener for login button
        loginButton.setOnClickListener {
            // Validate input fields (e.g., email and password)
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Perform login operation (e.g., authenticate user)
            // Dummy implementation for demonstration purposes
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Proceed to dashboard or next activity
                //val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish() // Finish the current activity
            } else {
                // Show error message if fields are empty
                // You can customize this part based on your app's requirements
                // For example, display a toast message or set an error on the EditText fields
            }
        }

        // Set click listener for register text view
        registerTextView.setOnClickListener {
            // Navigate to the registration activity
            //val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}