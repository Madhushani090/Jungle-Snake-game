package com.example.snakegame

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var highestScore: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportActionBar?.hide()

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SnakeGame", Context.MODE_PRIVATE)

        // Load highest score from SharedPreferences
        highestScore = sharedPreferences.getInt("highestScore", 0)
        updateHighestScoreText()

        ///////////////////////////////////////////////////////////////////////////////////////
        // touch control
        open class OnSwipeTouchListener : View.OnTouchListener {
            private val gestureDetector = GestureDetector(GestureListener())

            fun onTouch(event: MotionEvent): Boolean {
                return gestureDetector.onTouchEvent(event)
            }

            private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
                private val SWIPE_THRESHOLD = 100
                private val SWIPE_VELOCITY_THRESHOLD = 100

                override fun onDown(e: MotionEvent): Boolean {
                    return true
                }

                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    onTouch(e)
                    return true
                }

                override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                    val result = false
                    try {
                        val diffY = e2.y - e1.y
                        val diffX = e2.x - e1.x
                        if (abs(diffX) > abs(diffY)) {
                            if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                                if (diffX > 0) {
                                    onSwipeRight()
                                } else {
                                    onSwipeLeft()
                                }
                            }
                        } else {
                            // this is either a bottom or top swipe.
                            if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                                if (diffY > 0) {
                                    onSwipeTop()
                                } else {
                                    onSwipeBottom()
                                }
                            }
                        }
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                    return result
                }
            }

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                return gestureDetector.onTouchEvent(event)
            }

            open fun onSwipeRight() {}
            open fun onSwipeLeft() {}
            open fun onSwipeTop() {}
            open fun onSwipeBottom() {}
        }

        canvas.setOnTouchListener(object : OnSwipeTouchListener() {
            override fun onSwipeLeft() {
                Snake.alive = true
                if (Snake.direction != "right")
                    Snake.direction = "left"
            }

            override fun onSwipeRight() {
                Snake.alive = true
                if (Snake.direction != "left")
                    Snake.direction = "right"
            }

            override fun onSwipeTop() {
                Snake.alive = true
                if (Snake.direction != "up")
                    Snake.direction = "down"
            }

            override fun onSwipeBottom() {
                Snake.alive = true
                if (Snake.direction != "down")
                    Snake.direction = "up"
            }
        })

        // used to continuously move the snake while the game is running.
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                while (Snake.alive) {
                    when (Snake.direction) {
                        "up" -> {
                            // create new head position
                            Snake.headY -= 50
                            if (!Snake.possibleMove()) {
                                Snake.alive = false
                                Snake.reset()
                                Snake.score = 0 // Reset the score
                                runOnUiThread {
                                    start_game_score_text.text = "Score: ${Snake.score}"
                                }
                            }
                        }
                        "down" -> {
                            // create new head position
                            Snake.headY += 50
                            if (!Snake.possibleMove()) {
                                Snake.alive = false
                                Snake.reset()
                                Snake.score = 0 // Reset the score
                                runOnUiThread {
                                    start_game_score_text.text = "Score: ${Snake.score}"
                                }
                            }
                        }
                        "left" -> {
                            // create new head position
                            Snake.headX -= 50
                            if (!Snake.possibleMove()) {
                                Snake.alive = false
                                Snake.reset()
                                Snake.score = 0 // Reset the score
                                runOnUiThread {
                                    start_game_score_text.text = "Score: ${Snake.score}"
                                }
                            }
                        }
                        "right" -> {
                            // create new head position
                            Snake.headX += 50
                            if (!Snake.possibleMove()) {
                                Snake.alive = false
                                Snake.reset()
                                Snake.score = 0 // Reset the score
                                runOnUiThread {
                                    start_game_score_text.text = "Score: ${Snake.score}"
                                }
                            }
                        }
                    }

                    // convert head to body
                    Snake.bodyParts.add(arrayOf(Snake.headX, Snake.headY))

                    // delete tail if not eat
                    if (Snake.headX == Food.posX && Snake.headY == Food.posY) {
                        Food.generate()
                        Snake.bodyParts.add(Snake.bodyParts.last()) // Increase snake length
                        Snake.score += 50 // Increase score
                        runOnUiThread {
                            start_game_score_text.text = "Score: ${Snake.score}"
                            updateScore()
                        }
                    } else {
                        Snake.bodyParts.removeAt(0)
                    }

                    //game speed in millisecond
                    canvas.invalidate()
                    delay(150)
                }
            }
        }

        button_up.setOnClickListener {
            Snake.alive = true
            if (Snake.direction != "down")
                Snake.direction = "up"
        }
        button_down.setOnClickListener {
            Snake.alive = true
            if (Snake.direction != "up")
                Snake.direction = "down"
        }
        button_left.setOnClickListener {
            Snake.alive = true
            if (Snake.direction != "right")
                Snake.direction = "left"
        }
        button_right.setOnClickListener {
            Snake.alive = true
            if (Snake.direction != "left")
                Snake.direction = "right"
        }
    }

    private fun saveHighScore(score: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("highestScore", score)
        editor.apply()
    }

    private fun updateHighestScoreText() {
        start_game_highscore_text.text = "Highest Score: $highestScore"
    }

    private fun updateScoreText() {
        start_game_score_text.text = "Score: ${Snake.score}"
    }



    private fun updateScore() {
        if (Snake.score > highestScore) {
            highestScore = Snake.score
            saveHighScore(highestScore)
            updateHighestScoreText()


        }
        updateScoreText()
    }
}
