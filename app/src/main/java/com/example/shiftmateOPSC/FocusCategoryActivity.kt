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

        fetchCategoryDataForCurrentUser()

        selectCategoryButton.setOnClickListener {
            val selectedCategory = categorySpinner.selectedItem.toString()
            val intent = Intent(this, FocusActivity::class.java)
            intent.putExtra("selectedCategory", selectedCategory)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchCategoryDataForCurrentUser() {
        val currentUser = firebaseAuth.currentUser
        currentUser?.uid?.let { userId ->
            val tasksReference = firebaseDatabase.reference.child("TimeLog").child(userId)
            tasksReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (taskSnapshot in dataSnapshot.children) {
                        val category = taskSnapshot.child("category").getValue(String::class.java)
                        category?.let { categoryList.add(it) }
                    }
                    populateSpinner(categoryList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("FocusCategoryActivity", "Error fetching category data: ${databaseError.message}")
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
