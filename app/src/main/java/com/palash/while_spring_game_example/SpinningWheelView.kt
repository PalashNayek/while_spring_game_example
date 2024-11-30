package com.palash.while_spring_game_example

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class SpinningWheelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
    private var rectF = RectF()

    private val numbers = (0..9).toList()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = (width / 2).toFloat()
        rectF.set(0f, 0f, width.toFloat(), height.toFloat())

        val sweepAngle = 360f / numbers.size

        for (i in numbers.indices) {
            paint.color = if (i % 2 == 0) Color.BLUE else Color.RED

            canvas.drawArc(rectF, i * sweepAngle - 90, sweepAngle, true, paint)

            val angle = Math.toRadians((i * sweepAngle - 90 + sweepAngle / 2).toDouble())
            val textX = (width / 2 + (radius / 1.5) * cos(angle)).toFloat()
            val textY = (height / 2 + (radius / 1.5) * sin(angle)).toFloat()

            canvas.drawText(numbers[i].toString(), textX, textY, textPaint)
        }
    }
}


