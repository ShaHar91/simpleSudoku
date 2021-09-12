package com.shahar91.sudoku.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.appwise.room.BaseEntity
import com.shahar91.sudoku.data.database.enums.Difficulty
import com.shahar91.sudoku.data.database.utils.DBConstants

@Entity(tableName = DBConstants.SUDOKU_GAME_TABLE_NAME)
data class SudokuGame(
    @PrimaryKey
    override var id: Int,
    var game: String,
    var difficulty: Difficulty
): BaseEntity()