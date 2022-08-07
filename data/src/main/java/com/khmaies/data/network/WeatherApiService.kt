package com.khmaies.data.network

import com.khmaies.data.BuildConfig
import com.khmaies.data.model.Coordination
import com.khmaies.data.model.WeatherDataResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Created by Khmaies Hassen on 03,août,2022
 */
interface WeatherApiService {


    // <editor-fold desc="Get Requests">

    @GET("weather")
    suspend fun findCityCoord(
        @Query("q") q: String,
        @Query("appid") appid: String = BuildConfig.weather_api_key
    ): Response<Coordination>

    @GET("onecall")
    suspend fun findCityWeatherData(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("exclude") exclude: String = "hourly,daily",
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = BuildConfig.weather_api_key
    ): Response<WeatherDataResponse>

    // </editor-fold>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): WeatherApiService {

            val WS_SERVER_URL = BuildConfig.end_point
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(WS_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }
}