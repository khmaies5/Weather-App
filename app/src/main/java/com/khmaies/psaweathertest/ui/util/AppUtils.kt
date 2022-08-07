package com.khmaies.psaweathertest.ui.util

import android.annotation.SuppressLint
import com.khmaies.psaweathertest.R
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime(dateFormat: String): String =
        SimpleDateFormat(dateFormat).format(Date())

    @SuppressLint("SimpleDateFormat")
    fun isTimeExpired(dateTimeSavedWeather: String?): Boolean {
        dateTimeSavedWeather?.let {
            val currentDateTime = Date()
            val savedWeatherDateTime =
                SimpleDateFormat("E, d MMM yyyy HH:mm:ss").parse(it)
            val diff: Long = currentDateTime.time - savedWeatherDateTime.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            if (minutes > 10)
                return true
        }
        return false
    }

    fun getWeatherIcon(icon: String): Int {
       return when (icon) {
            "01d" -> R.drawable._01d
            "01n" -> R.drawable._01n
            "02d" -> R.drawable._02d
            "02n" -> R.drawable._02n
            "03d" -> R.drawable._03d
            "03n" -> R.drawable._03n
            "04d" -> R.drawable._04d
            "04n" -> R.drawable._04n
            "09d" -> R.drawable._09d
            "09n" -> R.drawable._09n
           "10n" -> R.drawable._10n
           "10d" -> R.drawable._10d
           "11d" -> R.drawable._11d
           "11n" -> R.drawable._11n
           "13d" -> R.drawable._13d
           "13n" -> R.drawable._13n
           "50d" -> R.drawable._50d
           "50n" -> R.drawable._50n
           else -> R.drawable._10n
       }
    }
}