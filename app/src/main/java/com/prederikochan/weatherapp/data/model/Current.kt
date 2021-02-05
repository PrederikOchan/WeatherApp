package com.prederikochan.weatherapp.data.model


import com.google.gson.annotations.SerializedName

data class Current(
    val dt: Int?,
    val temp: Double?,
    @SerializedName("feels_like")
    val feelsLike: Double?,
    val pressure: Int?,
    val humidity: Int?,
    @SerializedName("wind_speed")
    val windSpeed: Double?,
    val weather: List<Weather>?,
)