package com.shahar91.sudoku.data.database.enums

enum class Difficulty(val id: Int) {
    EASY(0),
    MEDIUM(1),
    HARD(2),
    EPIC(3);

    companion object {
        fun getDifficulty(id: Int): Difficulty {
            return values().find { it.id == id } ?: throw ClassCastException("Could not cast to correct Difficulty")
        }
    }
}