package com.shahar91.sudoku.di.modules

import com.shahar91.sudoku.data.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { AppDatabase.getDatabase(androidContext()) }
}