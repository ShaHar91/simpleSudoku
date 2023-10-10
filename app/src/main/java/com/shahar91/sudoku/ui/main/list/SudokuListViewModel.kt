package com.shahar91.sudoku.ui.main.list

import be.appwise.core.ui.base.BaseViewModel
import com.shahar91.sudoku.data.repositories.SudokuGameRepo

class SudokuListViewModel(
    private val sudokuGameRepo: SudokuGameRepo
) : BaseViewModel() {

    val sudokusLive = sudokuGameRepo.findAllSudokus()
}