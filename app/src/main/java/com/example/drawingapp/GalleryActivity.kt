package com.example.drawingapp

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class GalleryActivity : AppCompatActivity() {

    // Declare GridView and adapter components
    private lateinit var gridView: GridView
    private lateinit var imagePaths: MutableList<String>
    private lateinit var adapter: BaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the layout that contains the GridView
        setContentView(R.layout.activity_gallery)

        // Reference the GridView from the layout
        gridView = findViewById(R.id.gridView)

        // Retrieve drawing files and convert to mutable path list
        val drawingsDir = File(filesDir, "drawings")
        val imageFiles = drawingsDir.listFiles()?.toList() ?: emptyList()
        imagePaths = imageFiles.map { it.absolutePath }.toMutableList()

        // Create adapter for displaying image thumbnails
        adapter = object : BaseAdapter() {
            override fun getCount() = imagePaths.size
            override fun getItem(position: Int) = imagePaths[position]
            override fun getItemId(position: Int) = position.toLong()

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val imageView = ImageView(this@GalleryActivity)
                imageView.layoutParams = AbsListView.LayoutParams(500, 500)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP

                val bitmap = BitmapFactory.decodeFile(imagePaths[position])
                imageView.setImageBitmap(bitmap)

                // Long-press to prompt deletion of the image
                imageView.setOnLongClickListener {
                    showDeleteDialog(position)
                    true
                }

                return imageView
            }
        }

        // Set adapter to GridView to display images
        gridView.adapter = adapter
    }

    // Show confirmation dialog and handle deletion logic
    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete Drawing")
            .setMessage("Are you sure you want to delete this drawing?")
            .setPositiveButton("Yes") { _, _ ->
                val file = File(imagePaths[position])
                if (file.exists()) {
                    file.delete() // delete file from storage
                    imagePaths.removeAt(position) // remove path from list
                    adapter.notifyDataSetChanged() // update UI
                    Toast.makeText(this, "Drawing deleted", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null) // do nothing on cancel
            .show()
    }
}
