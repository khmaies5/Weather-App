package com.khmaies.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    const val BASE_URL = "https://api.openweathermap.org/data/2.5/onecall/"

    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiService: WeatherApiService by lazy {
        retrofitBuilder
            .build()
            .create(WeatherApiService::class.java)
    }
}