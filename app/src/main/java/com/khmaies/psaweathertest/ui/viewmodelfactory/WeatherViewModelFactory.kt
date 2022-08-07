package com.khmaies.psaweathertest.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khmaies.data.repository.WeatherRepository
import com.khmaies.psaweathertest.ui.viewmodel.WeatherViewModel


class WeatherViewModelFactory(
    private val repository: WeatherRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(repository) as T
    }
}