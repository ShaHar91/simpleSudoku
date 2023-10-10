package com.shahar91.sudoku.di.modules

import com.shahar91.sudoku.data.repositories.SudokuGameRepo
import com.shahar91.sudoku.data.repositories.SudokuRepo
import org.koin.dsl.module

val repositoryModule = module {

    single { SudokuRepo(get()) }
    single { SudokuGameRepo(get()) }
}