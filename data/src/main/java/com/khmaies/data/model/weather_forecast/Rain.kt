package com.khmaies.data.model.weather_forecast

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double
)
