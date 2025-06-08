package com.example.drawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.io.File
import java.io.FileOutputStream

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
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvasBitmap = Canvas(bitmap)
    }

    // Draw current bitmap and path to screen
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null) // permanent drawing
        canvas.drawPath(drawPath, paint) // active drawing path
    }

    // Handle touch events to draw with finger
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> drawPath.moveTo(x, y) // start path
            MotionEvent.ACTION_MOVE -> drawPath.lineTo(x, y) // continue path
            MotionEvent.ACTION_UP -> {
                canvasBitmap.drawPath(drawPath, paint) // draw path to bitmap
                drawPath.reset() // reset for next stroke
            }
        }

        invalidate() // redraw view
        return true
    }

    // Clear the canvas by filling it with white
    fun clearCanvas() {
        bitmap.eraseColor(Color.WHITE)
        invalidate()
    }

    // Save the drawing to internal storage as a PNG file
    fun saveDrawingToFile(context: Context): File? {
        val drawingsDir = File(context.filesDir, "drawings")
        if (!drawingsDir.exists()) {
            drawingsDir.mkdirs()
        }

        val file = File(drawingsDir, "drawing_${System.currentTimeMillis()}.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    }
}
