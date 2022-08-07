package com.khmaies.psaweathertest.ui.di

import android.content.Context
import com.khmaies.data.local.WeatherDatabase
import com.khmaies.data.network.NetworkConnectionInterceptor
import com.khmaies.data.network.WeatherApiService
import com.khmaies.data.repository.WeatherRepository
import com.khmaies.psaweathertest.ui.viewmodelfactory.WeatherViewModelFactory

class AppContainer(private val applicationContext: Context) {

    val viewModelFactory: WeatherViewModelFactory by lazy {
        WeatherViewModelFactory(repository)
    }


    private val apiService: WeatherApiService by lazy {
        WeatherApiService(networkConnectionInterceptor)
    }

    private val weatherDatabase: WeatherDatabase by lazy {
        WeatherDatabase.getDatabase(applicationContext)
    }

    private val repository: WeatherRepository by lazy {
        WeatherRepository(apiService, weatherDatabase.getWeatherDao())
    }

    private val networkConnectionInterceptor: NetworkConnectionInterceptor by lazy {
        NetworkConnectionInterceptor(applicationContext)
    }
}