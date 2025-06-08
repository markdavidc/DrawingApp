package com.example.drawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.createBitmap

// Custom view for drawing on screen with touch input
class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // Initialize path and paint for drawing
    private val drawPath = Path()
    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    // Declare bitmap and canvas used for permanent drawing
    private lateinit var bitmap: Bitmap
    private lateinit var canvasBitmap: Canvas

    // Create bitmap and canvas when view size is determined
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvasBitmap = Canvas(bitmap)
    }

    // Draw current bitmap and path to screen
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null) // permanent drawing
        canvas.drawPath(drawPath, paint) // active drawing path
    }
    // Required override to support accessibility
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    // Handle touch events to draw with finger
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> drawPath.moveTo(x, y)
            MotionEvent.ACTION_MOVE -> drawPath.lineTo(x, y)
            MotionEvent.ACTION_UP -> {
                canvasBitmap.drawPath(drawPath, paint)
                drawPath.reset()
                performClick()
            }
        }

        invalidate()
        return true
    }

    // Clear the canvas by filling it with white
    fun clearCanvas() {
        bitmap.eraseColor(Color.WHITE)
        invalidate()
    }

    // Save the drawing to internal storage as a PNG file
    fun saveDrawingToFile(context: Context): File {
        val drawingsDir = File(context.filesDir, "drawings") // Directory to save drawings
        if (!drawingsDir.exists()) {
            drawingsDir.mkdirs()  // Create directory if it doesn't exist
        }
        // Create unique file name based on current time
        val file = File(drawingsDir, "drawing_${System.currentTimeMillis()}.png")
        // Save bitmap as PNG to file
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    }
}
