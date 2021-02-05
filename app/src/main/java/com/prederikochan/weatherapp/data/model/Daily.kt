package com.prederikochan.weatherapp.data.model


import com.google.gson.annotations.SerializedName

data class Daily(
    val dt: Int?,
    val temp: Temp?,
    @SerializedName("feels_like")
    val feelsLike: FeelsLike?,
    val pressure: Int?,
    val humidity: Int?,
    val weather: List<WeatherX>?,
)