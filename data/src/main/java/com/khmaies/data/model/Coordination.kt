package com.khmaies.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Coordination(
    @SerializedName("coord") var coord: Coord,
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("sys") var sys: Sys,
) {
    @Keep
    data class Coord(
        @SerializedName("lon") var lon: Double? = null,
        @SerializedName("lat") var lat: Double? = null

    )

    @Keep
    data class Sys(
        @SerializedName("country") var country: String
    )
}
