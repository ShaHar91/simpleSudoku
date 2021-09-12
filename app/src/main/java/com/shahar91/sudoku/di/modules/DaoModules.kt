package com.shahar91.sudoku.di.modules

import com.shahar91.sudoku.data.database.AppDatabase
import org.koin.dsl.module

val daoModule = module {

    single { get<AppDatabase>().sudokuDao() }
    single { get<AppDatabase>().sudokuGameDao() }
}