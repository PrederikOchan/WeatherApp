package com.prederikochan.weatherapp.data.network

import android.content.Context
import com.prederikochan.weatherapp.data.model.WeatherDataResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeServices {
    class Builder {
        fun create(): HomeServices {
            return NetworkClient.create(HomeServices::class.java)
        }
    }

    @GET("data/2.5/onecall")
    fun getWeatherDataService(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "minutely,hourly,alerts"
    ) : Observable<WeatherDataResponse>
}