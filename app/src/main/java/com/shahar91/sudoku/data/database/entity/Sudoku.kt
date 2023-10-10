package com.shahar91.sudoku.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.appwise.room.BaseEntity
import com.shahar91.sudoku.data.database.utils.DBConstants

@Entity(tableName = DBConstants.SUDOKU_TABLE_NAME)
data class Sudoku(
    @PrimaryKey
    override var id: Int,
    var time: Long,
    var score: Int,
    var gameId: Int
) : BaseEntity()