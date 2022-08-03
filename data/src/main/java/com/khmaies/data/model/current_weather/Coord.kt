package com.khmaies.data.model.current_weather

/**
 * Created by Khmaies Hassen on 03,août,2022
 */
import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double
)