package com.khmaies.psaweathertest.ui.common

import android.app.Application
import com.khmaies.psaweathertest.ui.di.AppContainer

class WeatherApp : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }
}