package com.shahar91.sudoku.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import be.appwise.room.BaseRoomDao
import com.shahar91.sudoku.data.database.entity.SudokuGame
import com.shahar91.sudoku.data.database.utils.DBConstants

@Dao
abstract class SudokuGameDao : BaseRoomDao<SudokuGame>(DBConstants.SUDOKU_GAME_TABLE_NAME) {

    @Query("SELECT * FROM ${DBConstants.SUDOKU_GAME_TABLE_NAME}")
    abstract fun findAllLive(): LiveData<List<SudokuGame>>

    @Query("SELECT * FROM ${DBConstants.SUDOKU_GAME_TABLE_NAME} WHERE id = :id")
    abstract fun findSudokuGameById(id: Int): LiveData<SudokuGame>
}