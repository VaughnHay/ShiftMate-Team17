package com.example.shiftmateOPSC

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FocusCategoryActivity : AppCompatActivity() {

    private lateinit var categorySpinner: Spinner
    private lateinit var selectCategoryButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var categoryList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.focus_category_layout)

        categorySpinner = findViewById(R.id.categorySpinner)
        selectCategoryButton = findViewById(R.id.selectCategoryButton)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        categoryList = mutableListOf()

        fetchGoalDataForCurrentUser()

        selectCategoryButton.setOnClickListener {
            val selectedCategory = categorySpinner.selectedItem.toString()
            val intent = Intent(this, FocusActivity::class.java)
            intent.putExtra("selectedCategory", selectedCategory)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchGoalDataForCurrentUser() {
        val currentUser = firebaseAuth.currentUser
        currentUser?.uid?.let { userId ->
            val goalsReference = firebaseDatabase.reference.child("Goals")
            goalsReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (goalSnapshot in dataSnapshot.children) {
                        val goalUserId = goalSnapshot.child("userId").getValue(String::class.java)
                        if (goalUserId == userId) {
                            val goalName = goalSnapshot.child("goalName").getValue(String::class.java)
                            goalName?.let { categoryList.add(it) }
                        }
                    }
                    populateSpinner(categoryList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("FocusCategoryActivity", "Error fetching goal data: ${databaseError.message}")
                }
            })
        } ?: run {
            Log.e("FocusCategoryActivity", "No user logged in")
        }
    }

    private fun populateSpinner(categoryList: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }
}
