package com.example.shiftmateOPSC

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var bckButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)

        mAuth = FirebaseAuth.getInstance()
        usersRef = FirebaseDatabase.getInstance().getReference("Users")

        nameEditText = findViewById(R.id.regNameEditText)
        surnameEditText = findViewById(R.id.regSurnameEditText)
        emailEditText = findViewById(R.id.regEmailEditText)
        passwordEditText = findViewById(R.id.regPasswordEditText)
        registerButton = findViewById(R.id.regRegisterBut)
        bckButton = findViewById(R.id.regBackBut)

        registerButton.setOnClickListener {
            registerUser()
        }
        bckButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {
        val name = nameEditText.text.toString().trim()
        val surname = surnameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid ?: ""
                    val userData = Users(userId, name, surname, email)

                    // Save user data to Firebase Realtime Database
                    usersRef.child(userId).setValue(userData)
                        .addOnCompleteListener {
                            // Registration successful, navigate to profile page
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT)
                                .show()

                            // Pass user data to profile page using intent extras
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.putExtra("userId", userId)
                            intent.putExtra("name", name)
                            intent.putExtra("surname", surname)
                            intent.putExtra("email", email)
                            startActivity(intent)
                            finish() // Close the register activity
                        }
                        .addOnFailureListener { err ->
                            Toast.makeText(
                                this,
                                "Error saving user data: ${err.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error creating user: ${err.message}", Toast.LENGTH_LONG)
                    .show()
            }
    }
}