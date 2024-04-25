package com.example.shiftmateOPSC
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddTask : AppCompatActivity() {

    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var categoryAutoCompleteTextView: AutoCompleteTextView
    private lateinit var saveButton: Button
    private lateinit var takePictureButton: Button
    private lateinit var imageView: ImageView

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

        // Set onClickListener for the save button
        saveButton.setOnClickListener {
            // Get values from EditText and AutoCompleteTextView
            val startTime = startTimeEditText.text.toString()
            val endTime = endTimeEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val category = categoryAutoCompleteTextView.text.toString()

            // Check if any of the fields are empty
            if (startTime.isEmpty() || endTime.isEmpty() || description.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // All fields are filled, temporarily displaying in toast message
                val message = "Start Time: $startTime\nEnd Time: $endTime\nDescription: $description\nCategory: $category"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        // Set onClickListener for the take picture button
        takePictureButton.setOnClickListener {
            // Here you can implement the logic to capture a photograph
            // For simplicity, I'm just toggling the visibility of the image view
            if (imageView.visibility == View.VISIBLE) {
                imageView.visibility = View.GONE
            } else {
                imageView.visibility = View.VISIBLE
            }
        }
    }
}