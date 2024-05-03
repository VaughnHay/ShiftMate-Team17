package com.example.shiftmateOPSC

import android.Manifest
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class AddTask : AppCompatActivity() {

    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firebaseDatabase: FirebaseDatabase

    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var categoryACTV: EditText // Added
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
        startTimeEditText.isFocusable = false
        endTimeEditText.isFocusable = false
        descriptionEditText = findViewById(R.id.DescriptionTV)
        categorySpinner = findViewById(R.id.CategorySpin)
        categoryACTV = findViewById(R.id.CategoryACTV) // Added
        categoryACTV.visibility = View.GONE // Hide CategoryACTV by default
        saveButton = findViewById(R.id.btnSave)
        takePictureButton = findViewById(R.id.btnTakePic)
        imageView = findViewById(R.id.image_view)
        calendarView = findViewById(R.id.calendarView3)
        backButton = findViewById(R.id.btnBack)

        // Set onClickListener for the save button
        saveButton.setOnClickListener {
            // Get values from EditText, Spinner, and CalendarView
            val startTime = startTimeEditText.text.toString()
            val endTime = endTimeEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val category = if (categoryACTV.visibility == View.VISIBLE)
                categoryACTV.text.toString() // If CategoryACTV is visible, get text from it
            else
                categorySpinner.selectedItem.toString() // Otherwise, get selected item from Spinner
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

        // Set OnClickListeners for start and end time EditTexts
        startTimeEditText.setOnClickListener {
            showTimePickerDialog(startTimeEditText)
        }

        endTimeEditText.setOnClickListener {
            showTimePickerDialog(endTimeEditText)
        }

        // Set onClickListener for the back button
        backButton.setOnClickListener {
            finish()
        }

        // Retrieve category data for the current user from Firebase and populate Spinner
        fetchCategoryDataForCurrentUser()
    }

    private fun fetchCategoryDataForCurrentUser() {
        val currentUser = firebaseAuth.currentUser
        currentUser?.uid?.let { userId ->
            val categoryList = ArrayList<String>()
            val tasksReference = firebaseDatabase.reference.child("TimeLog").child(userId)
            tasksReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (taskSnapshot in dataSnapshot.children) {
                        val category = taskSnapshot.child("category").getValue(String::class.java)
                        category?.let { categoryList.add(it) }
                    }
                    if (categoryList.isEmpty()) {
                        // Show CategoryACTV if category list is empty
                        categoryACTV.visibility = View.VISIBLE
                    } else {
                        // Hide CategoryACTV and populate Spinner
                        categoryACTV.visibility = View.GONE
                        populateSpinner(categoryList)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("AddTask", "Error fetching category data for current user: ${databaseError.message}")
                }
            })
        } ?: run {
            Log.e("AddTask", "No user logged in")
        }
    }

    private fun populateSpinner(categoryList: List<String>) {
        val categoriesWithAddNew = categoryList.toMutableList()
        categoriesWithAddNew.add("Add New") // Add "Add New" option to the list

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriesWithAddNew)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if (selectedItem == "Add New") {
                    // Show CategoryACTV if "Add New" is selected
                    categoryACTV.visibility = View.VISIBLE
                } else {
                    // Hide CategoryACTV
                    categoryACTV.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }



    // Function to show TimePickerDialog
    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editText.setText(selectedTime)
            },
            hour,
            minute,
            true // Set this to true to show minutes
        )

        timePickerDialog.show()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }

    private fun uploadImageToFirebaseStorage(
        bitmap: Bitmap,
        startTime: String,
        endTime: String,
        description: String,
        category: String,
        date: String
    ) {
        val currentUser = firebaseAuth.currentUser
        val storageReference =
            firebaseStorage.reference.child("images/${currentUser?.uid}/${System.currentTimeMillis()}.jpg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        val uploadTask = storageReference.putBytes(imageData)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                saveTaskToFirebaseDatabase(
                    imageUrl,
                    startTime,
                    endTime,
                    description,
                    category,
                    date
                )
            }
        }.addOnFailureListener { exception ->
            // Handle error
        }
    }

    private fun saveTaskToFirebaseDatabase(
        imageUrl: String?,
        startTime: String,
        endTime: String,
        description: String,
        category: String,
        date: String
    ) {
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


