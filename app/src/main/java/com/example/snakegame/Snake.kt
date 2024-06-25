package com.example.snakegame

class Snake {
    companion object {
        // default: just one body part
        var headX = 0f
        var headY = 0f
        var bodyParts = mutableListOf(arrayOf(0f, 0f))  //  mutable list containing arrays of floats representing snake's body parts
        var direction = "right"
        var alive = false
        var score = 0
        var highestScore = 0

        fun possibleMove(): Boolean {
            return headX in 0f..1000f && headY in 0f..1000f   //that returns true if the snake's head is within the game grid
        }

        // resets the snake's position,
        fun reset() {
            headX = 0f
            headY = 0f
            bodyParts = mutableListOf(arrayOf(0f, 0f))
            direction = "right"
        }


    }
}
