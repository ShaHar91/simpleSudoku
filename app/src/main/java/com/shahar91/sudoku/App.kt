package com.shahar91.sudoku

import android.app.Application
import be.appwise.core.core.CoreApp
import com.shahar91.sudoku.di.KoinInitializer

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        KoinInitializer.init(this)

        CoreApp.init(this)
            .apply {
                if (BuildConfig.DEBUG) {
                    initializeErrorActivity(true)
                }
            }
            .initializeLogger(getString(R.string.app_name), BuildConfig.DEBUG)
            .build()
    }
}