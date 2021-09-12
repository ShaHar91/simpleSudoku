package com.shahar91.sudoku.di

import android.content.Context
import com.shahar91.sudoku.BuildConfig
import com.shahar91.sudoku.di.modules.appModule
import com.shahar91.sudoku.di.modules.daoModule
import com.shahar91.sudoku.di.modules.repositoryModule
import com.shahar91.sudoku.di.modules.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

object KoinInitializer {
    fun init(context: Context) {
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            androidContext(context)
            modules(appModule, daoModule, viewModelModule, repositoryModule)
        }
    }
}