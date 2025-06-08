package com.example.drawingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize MainActivity with layout setup
        setContentView(R.layout.activity_main)

        // Reference UI elements from layout
        drawingView = findViewById(R.id.drawing_view)
        val saveButton: Button = findViewById(R.id.save_button)
        val clearButton: Button = findViewById(R.id.clear_button)
        val galleryButton: Button = findViewById(R.id.gallery_button)

        // Implement save button functionality
        saveButton.setOnClickListener {
            val savedFile = drawingView.saveDrawingToFile(this)
            if (savedFile != null) {
                Toast.makeText(this, "Drawing saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error saving drawing.", Toast.LENGTH_SHORT).show()
            }
        }

        // Add clear button logic to reset canvas
        clearButton.setOnClickListener {
            drawingView.clearCanvas()
        }

        // Enable navigation to gallery screen
        galleryButton.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }
    }
}
