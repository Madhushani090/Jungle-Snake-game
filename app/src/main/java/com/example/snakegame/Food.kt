package com.example.snakegame

class Food {
    companion object {
        //store the position of the food
        var posX = 500f
        var posY = 500f

        //used to generate a new position for the food with in the game grid
        fun generate() {
            posX = (1..20).random().toFloat() * 50
            posY = (1..20).random().toFloat() * 50
        }
    }
}