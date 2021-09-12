package com.shahar91.sudoku.data.database.dao

import androidx.room.Dao
import be.appwise.room.BaseRoomDao
import com.shahar91.sudoku.data.database.entity.SudokuGame
import com.shahar91.sudoku.data.database.utils.DBConstants

@Dao
abstract class SudokuGameDao : BaseRoomDao<SudokuGame>(DBConstants.SUDOKU_GAME_TABLE_NAME)