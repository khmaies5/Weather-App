package com.khmaies.data.model.current_weather

/**
 * Created by Khmaies Hassen on 03,août,2022
 */
import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("deg")
    val deg: Int,
    @SerializedName("speed")
    val speed: Double
)