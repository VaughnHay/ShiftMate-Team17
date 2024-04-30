package com.example.shiftmateOPSC

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class AddTask : AppCompatActivity() {

    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firebaseDatabase: FirebaseDatabase

    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var categoryAutoCompleteTextView: AutoCompleteTextView
    private lateinit var saveButton: Button
    private lateinit var takePictureButton: Button
    private lateinit var imageView: ImageView
    private lateinit var calendarView: CalendarView
    private lateinit var dateSelected: String
    private var imageBitmap: Bitmap? = null
    private lateinit var backButton: Button

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val CAMERA_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_task_layout)

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        // Initialize views
        startTimeEditText = findViewById(R.id.StartTimeTV)
        endTimeEditText = findViewById(R.id.EndTimeTV)
        descriptionEditText = findViewById(R.id.DescriptionTV)
        categoryAutoCompleteTextView = findViewById(R.id.CategoryACTV)
        saveButton = findViewById(R.id.btnSave)
        takePictureButton = findViewById(R.id.btnTakePic)
        imageView = findViewById(R.id.image_view)
        calendarView = findViewById(R.id.calendarView3)
        backButton = findViewById(R.id.btnBack)

        // Set onClickListener for the save button
        saveButton.setOnClickListener {
            // Get values from EditText, AutoCompleteTextView, and CalendarView
            val startTime = startTimeEditText.text.toString()
            val endTime = endTimeEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val category = categoryAutoCompleteTextView.text.toString()
            val date = dateSelected

            // Check if any of the mandatory fields are empty
            if (startTime.isEmpty() || endTime.isEmpty() || description.isEmpty() || category.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all mandatory fields", Toast.LENGTH_SHORT).show()
            } else {
                // All mandatory fields are filled
                // Upload image to Firebase Storage
                if (imageBitmap != null) {
                    uploadImageToFirebaseStorage(imageBitmap!!, startTime, endTime, description, category, date)
                } else {
                    saveTaskToFirebaseDatabase(null, startTime, endTime, description, category, date)
                }
                Toast.makeText(
                    this, "Task Saved.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        backButton.setOnClickListener{
            finish()
        }

        // Set onClickListener for the take picture button
        takePictureButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            } else {
                openCamera()
            }
        }

        // Set OnDateChangeListener for the calendar view
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Store the selected date in the format you desire
            dateSelected = "$year-${month + 1}-$dayOfMonth"
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        } else {
            // Notify the user that the camera app is not available
            Toast.makeText(this, "Camera app is not available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val extras = data?.extras
            imageBitmap = extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            imageView.visibility = View.VISIBLE
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }

    private fun uploadImageToFirebaseStorage(bitmap: Bitmap, startTime: String, endTime: String, description: String, category: String, date: String) {
        val currentUser = firebaseAuth.currentUser
        val storageReference = firebaseStorage.reference.child("images/${currentUser?.uid}/${System.currentTimeMillis()}.jpg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        val uploadTask = storageReference.putBytes(imageData)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                saveTaskToFirebaseDatabase(imageUrl, startTime, endTime, description, category, date)
            }
        }.addOnFailureListener { exception ->
            // Handle error
        }
    }

    private fun saveTaskToFirebaseDatabase(imageUrl: String?, startTime: String, endTime: String, description: String, category: String, date: String) {
        val currentUser = firebaseAuth.currentUser
        val userUid = currentUser?.uid ?: return  // Return if user is null

        val tasksReference = firebaseDatabase.reference.child("TimeLog").child(userUid)

        val taskMap = HashMap<String, Any>()
        taskMap["startTime"] = startTime
        taskMap["endTime"] = endTime
        taskMap["description"] = description
        taskMap["category"] = category
        taskMap["date"] = date
        imageUrl?.let { taskMap["imageUrl"] = it }

        val taskId = tasksReference.push().key ?: ""
        tasksReference.child(taskId).setValue(taskMap)
            .addOnSuccessListener {
                // Task saved successfully
                // You can add any further action here if needed
            }.addOnFailureListener { exception ->
                // Handle error
            }
    }
}
