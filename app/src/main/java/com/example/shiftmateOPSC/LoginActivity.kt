package com.example.shiftmateOPSC

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
class LoginActivity: AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        mAuth = FirebaseAuth.getInstance()
        // Initialize views
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)

        loginButton.setOnClickListener {
            loginUser()
        }

        // Set click listener for register text view
        registerButton.setOnClickListener {
            // Navigate to the registration activity
            val intent = Intent(this@LoginActivity, Register::class.java)
            startActivity(intent)
        }
    }
    private fun loginUser() {
        val userEmail = emailEditText.text.toString()
        val userPassword = passwordEditText.text.toString()

        if (userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
            mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser
                        Toast.makeText(
                            this, "Login successful.",
                            Toast.LENGTH_SHORT
                        ).show()
                        // You can navigate to another activity or perform any other action here
                        // For example:
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this, "Login failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
        }
    }
}