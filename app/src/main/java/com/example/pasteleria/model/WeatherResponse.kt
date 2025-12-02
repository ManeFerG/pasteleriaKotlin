package com.example.pasteleria.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("timezone") val timezone: String,
    @SerializedName("current_weather") val currentWeather: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("temperature") val temperature: Double, // °C
    @SerializedName("weathercode") val weatherCode: Int
)
