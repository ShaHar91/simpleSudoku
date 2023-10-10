package com.shahar91.sudoku.data.database.dao

import androidx.room.Dao
import be.appwise.room.BaseRoomDao
import com.shahar91.sudoku.data.database.entity.Sudoku
import com.shahar91.sudoku.data.database.utils.DBConstants

@Dao
abstract class SudokuDao : BaseRoomDao<Sudoku>(DBConstants.SUDOKU_TABLE_NAME)