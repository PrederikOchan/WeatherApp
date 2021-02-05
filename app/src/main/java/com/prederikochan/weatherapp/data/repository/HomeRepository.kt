package com.prederikochan.weatherapp.data.repository

import com.prederikochan.weatherapp.data.model.WeatherDataResponse
import com.prederikochan.weatherapp.data.network.HomeServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeRepository {

    fun fetchCurrentWeather(
        latitude: String,
        longitude: String,
        success: (response: WeatherDataResponse) -> Unit,
        failed: (exception: Throwable) -> Unit
    ) {

        val response = HomeServices.Builder().create()
            .getWeatherDataService(latitude, longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    success(it)
                },
                {
                    failed(it)
                }
            )

    }

}