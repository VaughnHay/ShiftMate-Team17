package com.example.shiftmateOPSC

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.shiftmateOPSC.databinding.ProfileLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import android.content.Intent


class Profile : AppCompatActivity() {
    private lateinit var binding: ProfileLayoutBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        usersRef = FirebaseDatabase.getInstance().getReference("Users")

        val currentUserID = mAuth.currentUser?.uid
        currentUserID?.let { uid ->
            usersRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userName = snapshot.child("userName").getValue(String::class.java)
                        val userSurname = snapshot.child("userSurname").getValue(String::class.java)
                        val userEmail = snapshot.child("userEmail").getValue(String::class.java)

                        binding.profileTitleTextView.text = "Your Profile"
                        binding.profileNameEditText.setText("First Name: $userName")
                        binding.profileSurnameEditText.setText("Surname: $userSurname")
                        binding.profileEmailEditText.setText("Email: $userEmail")
                        binding.profilePasswordEditText.setText("Password: *********")

                        // Spinner setup
                        val roleSpinner: Spinner = findViewById(R.id.profileRoleSpinner)
                        ArrayAdapter.createFromResource(
                            this@Profile,
                            R.array.role_options,
                            android.R.layout.simple_spinner_item
                        ).also { adapter ->
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            roleSpinner.adapter = adapter
                        }
                        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                val selectedItem = parent?.getItemAtPosition(position).toString()
                                // Handle the selected role as needed
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Handle case where nothing is selected
                            }
                        }

                        // Back button
                        val backButton: Button = findViewById(R.id.profileBackBut)
                        backButton.setOnClickListener {
                            startActivity(Intent(this@Profile, DashboardActivity::class.java))
                            finish()
                        }

                        // Save button
                        val saveButton: Button = findViewById(R.id.profileSaveBut)
                        saveButton.setOnClickListener {
                            // Display a toast message
                            Toast.makeText(this@Profile, "User profile updated", Toast.LENGTH_SHORT).show()
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
