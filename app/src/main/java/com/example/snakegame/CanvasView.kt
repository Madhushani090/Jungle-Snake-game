package com.example.snakegame

import android.view.View
import android.util.AttributeSet
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.BitmapFactory


class CanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //sets snake body
        val snakeBody = Paint()
        snakeBody.color = Color.GREEN
        snakeBody.style = Paint.Style.FILL

        //sets food properties
        val food = Paint()
        food.color = Color.RED
        food.style = Paint.Style.FILL

        //sets game space properties

        val darkBlue = Color.rgb(0, 0, 139)
        val level = Paint()
        level.color = darkBlue
        level.style = Paint.Style.FILL


        val cornerRadius = 30f // game space corners
        // Draw level background with rounded corners
        canvas?.drawRoundRect(0f, 0f, 1050f, 1050f, cornerRadius, cornerRadius, level)

        // Draw snake as circles
        for (i in Snake.bodyParts) {
            canvas?.drawCircle(i[0] + 22.5f, i[1] + 22.5f, 22.5f, snakeBody)
        }

        // Draw food as rectangles
        canvas?.drawRect(Food.posX, Food.posY, Food.posX + 45f, Food.posY + 45f, food)

    }

}


