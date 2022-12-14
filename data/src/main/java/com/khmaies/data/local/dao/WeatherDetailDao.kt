package com.khmaies.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khmaies.data.model.WeatherDetail

@Dao
interface WeatherDetailDao {
    /**
     * Duplicate values are replaced in the table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeather(weatherDetail: WeatherDetail)

    @Query("SELECT * FROM ${WeatherDetail.TABLE_NAME} WHERE cityName = :cityName")
    suspend fun fetchWeatherByCity(cityName: String): WeatherDetail?

    @Query("DELETE FROM ${WeatherDetail.TABLE_NAME} WHERE id = :cityId")
    suspend fun deleteCity(cityId: Int)


    @Query("SELECT * FROM ${WeatherDetail.TABLE_NAME}  ORDER BY dateTime DESC LIMIT 1")
    suspend fun fetchDefaultWeather(): WeatherDetail?

    @Query("SELECT * FROM ${WeatherDetail.TABLE_NAME}")
    suspend fun fetchAllWeatherDetails(): List<WeatherDetail>
}