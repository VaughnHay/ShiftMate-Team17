package com.example.shiftmateOPSC

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

data class LineChartData(
    val xValue: Float,
    val yValue: Float
)

class CustomLineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var data = listOf<LineChartData>()
        set(value) {
            field = value
            invalidate()
        }

    var minGoal: Float = 0f
    var maxGoal: Float = 0f

    private val linePaint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 8f
    }

    private val goalPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 4f
        style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.isEmpty()) {
            canvas.drawText("No Data", width / 2f, height / 2f, linePaint)
            return
        }

        val maxYValue = data.maxOfOrNull { it.yValue } ?: 0f
        val scaleY = height / maxYValue

        // Draw the data line
        for (i in 0 until data.size - 1) {
            val startX = data[i].xValue * (width / data.size)
            val startY = height - data[i].yValue * scaleY
            val stopX = data[i + 1].xValue * (width / data.size)
            val stopY = height - data[i + 1].yValue * scaleY
            canvas.drawLine(startX, startY, stopX, stopY, linePaint)
        }

        // Draw the min and max goal lines
        val minGoalY = height - (minGoal * scaleY)
        val maxGoalY = height - (maxGoal * scaleY)
        canvas.drawLine(0f, minGoalY, width.toFloat(), minGoalY, goalPaint)
        canvas.drawLine(0f, maxGoalY, width.toFloat(), maxGoalY, goalPaint)
    }
}
