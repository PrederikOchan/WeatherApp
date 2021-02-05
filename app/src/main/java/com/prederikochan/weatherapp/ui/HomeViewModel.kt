package com.prederikochan.weatherapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.prederikochan.weatherapp.data.model.WeatherDataResponse
import com.prederikochan.weatherapp.data.repository.HomeRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val weatherDataObserver = MutableLiveData<WeatherDataResponse>()
    val errorObserver = MutableLiveData<Throwable>()

    private val homeRepo = HomeRepository()

    fun getWeatherData(
        latitude: String,
        longitude: String
    ) {
        homeRepo.fetchCurrentWeather(
            latitude,
            longitude,
            {
                weatherDataObserver.postValue(it)
            },
            {
                errorObserver.postValue(it)
            }
        )
    }
}