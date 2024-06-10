package com.example.shiftmateOPSC

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

data class GoalData(
    val goalName: String,
    val minHourGoal: Float,
    val maxHourGoal: Float
)

class CustomBarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val minBarPaint = Paint().apply {
        color = Color.parseColor("#FFA500")
        style = Paint.Style.FILL
    }

    private val maxBarPaint = Paint().apply {
        color = Color.parseColor("#FFA500")
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 50f
    }

    private val goalNamePaint = Paint().apply {
        color = Color.WHITE
        textSize = 40f
    }

    var data = listOf<GoalData>()
        set(value) {
            field = value
            Log.d("CustomBarChartView", "Data set: $value")
            invalidate() // Redraw the view with the new data
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("CustomBarChartView", "onDraw called")

        if (data.isEmpty()) {
            Log.d("CustomBarChartView", "No data to draw")
            return
        }

        val barWidth = width / (data.size * 3)
        val barSpacing = barWidth / 4
        val maxHeight = height - 200f
        val maxValue = getMaxValue()

        if (maxValue == 0f) {
            Log.d("CustomBarChartView", "Max value is 0")
            return
        }

        val textHeight = textPaint.descent() - textPaint.ascent()
        val maxTextHeight = textHeight * 2 // Double the text height for max values

        for ((i, goalData) in data.withIndex()) {
            val leftMin = i * 3 * barWidth
            val rightMin = leftMin + barWidth

            val leftMax = rightMin + barSpacing
            val rightMax = leftMax + barWidth

            val minValue = goalData.minHourGoal
            val maxValue = goalData.maxHourGoal

            // Draw min value bar
            val minTop = maxHeight - (minValue / getMaxValue() * maxHeight)
            canvas.drawRect(leftMin.toFloat(), minTop, rightMin.toFloat(), maxHeight, minBarPaint)
            canvas.drawText(minValue.toString(), leftMin + barWidth / 2f - textPaint.measureText(minValue.toString()) / 2, minTop - 20, textPaint)

            // Draw max value bar
            val maxTop = maxHeight - (maxValue / getMaxValue() * maxHeight)
            canvas.drawRect(leftMax.toFloat(), maxTop, rightMax.toFloat(), maxHeight, maxBarPaint)

            // Ensure max value text remains visible within view bounds
            val maxTextY = if (maxTop - maxTextHeight < 0) maxTop + maxTextHeight else maxTop - 20
            canvas.drawText(maxValue.toString(), leftMax + barWidth / 2f - textPaint.measureText(maxValue.toString()) / 2, maxTextY, textPaint)

            // Draw goal name centered between the two bars
            val goalNameX = (leftMin + rightMax) / 2f - goalNamePaint.measureText(goalData.goalName) / 2
            canvas.drawText(goalData.goalName, goalNameX, maxHeight + 50, goalNamePaint)
        }
    }

    private fun getMaxValue(): Float {
        return data.flatMap { listOf(it.minHourGoal, it.maxHourGoal) }.maxOrNull() ?: 0f
    }
}
