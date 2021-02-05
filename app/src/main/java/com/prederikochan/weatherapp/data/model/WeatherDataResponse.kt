package com.prederikochan.weatherapp.data.model


import com.google.gson.annotations.SerializedName

data class WeatherDataResponse(
    val lat: Double?,
    val lon: Double?,
    val timezone: String?,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int?,
    val current: Current?,
    val daily: List<Daily>?
)