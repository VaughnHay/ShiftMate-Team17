package com.example.shiftmateOPSC

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AddTask : AppCompatActivity() {

    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var categoryAutoCompleteTextView: AutoCompleteTextView
    private lateinit var saveButton: Button
    private lateinit var takePictureButton: Button
    private lateinit var imageView: ImageView
    private lateinit var calendarView: CalendarView
    private lateinit var dateSelected: String

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val CAMERA_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_task_layout)

        // Initialize views
        startTimeEditText = findViewById(R.id.StartTimeTV)
        endTimeEditText = findViewById(R.id.EndTimeTV)
        descriptionEditText = findViewById(R.id.DescriptionTV)
        categoryAutoCompleteTextView = findViewById(R.id.CategoryACTV)
        saveButton = findViewById(R.id.btnSave)
        takePictureButton = findViewById(R.id.btnTakePic)
        imageView = findViewById(R.id.image_view)
        calendarView = findViewById(R.id.calendarView3)

        // Set onClickListener for the save button
        saveButton.setOnClickListener {
            // Get values from EditText, AutoCompleteTextView, and CalendarView
            val startTime = startTimeEditText.text.toString()
            val endTime = endTimeEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val category = categoryAutoCompleteTextView.text.toString()
            val date = dateSelected

            // Check if any of the fields are empty
            if (startTime.isEmpty() || endTime.isEmpty() || description.isEmpty() || category.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // All fields are filled, temporarily displaying in toast message
                val message =
                    "Start Time: $startTime\nEnd Time: $endTime\nDescription: $description\nCategory: $category\nDate: $date"
                //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                showFullMessageDialog(message)
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
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
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
            val imageBitmap = extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            imageView.visibility = View.VISIBLE
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array
    <out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }

    private fun showFullMessageDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Task Details")
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()
    }
}

