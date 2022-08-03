package com.khmaies.data

import com.khmaies.data.model.current_weather.CurrentWeather
import com.khmaies.data.model.weather_forecast.ForecastWeather
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Khmaies Hassen on 03,août,2022
 */
interface WeatherApiService {


    //with city name
    @GET("weather?appid=${BuildConfig.WeatherAPIKey}&units=metric")
    suspend fun getCurrentWeather(
        @Query("q") location: String
    ) : CurrentWeather

    //with lat lng
    @GET("weather?appid=${BuildConfig.WeatherAPIKey}&units=metric")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ) : CurrentWeather

    //with name
    @GET("forecast?&appid=${BuildConfig.WeatherAPIKey}&units=metric")
    suspend fun getWeatherForecast(
        @Query("q") location: String
    ) : ForecastWeather

    //with lat long
    @GET("forecast?&appid=${BuildConfig.WeatherAPIKey}&units=metric")
    suspend fun getWeatherForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ) : ForecastWeather
}