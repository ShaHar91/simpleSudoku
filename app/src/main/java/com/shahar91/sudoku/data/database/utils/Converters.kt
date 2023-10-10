package com.shahar91.sudoku.data.database.utils

import androidx.room.TypeConverter
import com.shahar91.sudoku.data.database.enums.Difficulty

class Converters {
    @TypeConverter
    fun fromDifficulty(difficulty: Difficulty): Int {
        return difficulty.id
    }

    @TypeConverter
    fun toDifficulty(difficultyId: Int): Difficulty {
        return Difficulty.getDifficulty(difficultyId)
    }
}