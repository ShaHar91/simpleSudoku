package com.shahar91.sudoku.di.modules

import com.shahar91.sudoku.ui.main.home.HomeViewModel
import com.shahar91.sudoku.ui.main.list.SudokuListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { HomeViewModel() }
    viewModel { SudokuListViewModel() }
}