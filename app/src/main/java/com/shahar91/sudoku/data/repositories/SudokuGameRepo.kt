package com.shahar91.sudoku.data.repositories

import com.shahar91.sudoku.data.database.dao.SudokuGameDao

class SudokuGameRepo(
    private val sudokuGameDao: SudokuGameDao
) {

    fun findAllSudokus() = sudokuGameDao.findAllLive()
}