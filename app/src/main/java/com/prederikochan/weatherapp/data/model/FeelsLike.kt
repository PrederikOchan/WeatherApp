package com.prederikochan.weatherapp.data.model


import com.google.gson.annotations.SerializedName

data class FeelsLike(
    val day: Double?,
    val night: Double?,
    val eve: Double?,
    val morn: Double?
)