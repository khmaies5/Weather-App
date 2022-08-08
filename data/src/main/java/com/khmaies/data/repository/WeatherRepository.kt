package com.khmaies.data.repository

import com.khmaies.data.local.dao.WeatherDetailDao
import com.khmaies.data.network.SafeApiRequest
import com.khmaies.data.network.WeatherApiService
import com.khmaies.data.model.Coordination
import com.khmaies.data.model.WeatherDataResponse
import com.khmaies.data.model.WeatherDetail

class WeatherRepository(
    private val api: WeatherApiService,
    private val db: WeatherDetailDao
) : SafeApiRequest() {

    suspend fun findCityCoord(cityName: String): Coordination = apiRequest {
        api.findCityCoord(cityName)
    }

   suspend fun findCityWeather(weatherDataResponse: Coordination): WeatherDataResponse =
        apiRequest {
            api.findCityWeatherData(weatherDataResponse.coord.lat, weatherDataResponse.coord.lon)
        }

    suspend fun deleteCity(cityId: Int) {
        db.deleteCity(cityId)
    }

    suspend fun addWeather(weatherDetail: WeatherDetail) {
        db.addWeather(weatherDetail)
    }

    suspend fun fetchWeatherDetail(cityName: String): WeatherDetail? =
        db.fetchWeatherByCity(cityName)

    suspend fun fetchAllWeatherDetails(): List<WeatherDetail> =
        db.fetchAllWeatherDetails()

    suspend fun fetchLatestWeatherDetail() : WeatherDetail? =
        db.fetchDefaultWeather()
}