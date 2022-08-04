package com.khmaies.data.repository

import com.khmaies.data.SafeApiRequest
import com.khmaies.data.WeatherApiService
import com.khmaies.data.model.WeatherDataResponse

class WeatherRepository(
    private val api: WeatherApiService
) : SafeApiRequest() {

    suspend fun findCityCoord(cityName: String): WeatherDataResponse.Coord = apiRequest {
        api.findCityCoord(cityName)
    }

    suspend fun findCityWeather(lat: Double, lon: Double): WeatherDataResponse = apiRequest {
        api.findCityWeatherData(lat, lon)
    }
}