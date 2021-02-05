package com.prederikochan.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.prederikochan.weatherapp.R
import com.prederikochan.weatherapp.data.model.WeatherDataResponse
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeListAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initUI()
        initObserver()
    }

    private fun initObserver() {
        viewModel.weatherDataObserver.observe(this, {
            swipeRefresh.isRefreshing = false
            stopShimmer()
            dataAvailable()
            updateUI(it)
        })
        viewModel.errorObserver.observe(this, {
            swipeRefresh.isRefreshing = false
            stopShimmer()
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
        })
    }

    private fun updateUI(data: WeatherDataResponse) {
        txtCityName.text = data.timezone?.substringAfterLast("/") ?: ""
        txtCondition.text = data.current?.weather?.get(0)?.main ?: ""
        txtTemp.text = "${data.current?.temp?.toInt()}°C"
        txtRealFeel.text = "${data.current?.feelsLike?.toInt()}°C"
        txtHumidity.text = "${data.current?.humidity}%"
        txtWindSpeed.text = "${data.current?.windSpeed}m/s"
        txtPressure.text = "${data.current?.pressure}hPa"

        Glide.with(this)
            .load("http://openweathermap.org/img/wn/${data.current?.weather?.get(0)?.icon}@2x.png")
            .into(icWeather)

        adapter.updateData(data.daily!!)
    }

    private fun initUI() {
        adapter = HomeListAdapter(this)
        listFutureWeather.adapter = adapter
        setupSwipeRefresh()
        startShimmer()
    }

    private fun setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            enableMyLocation()
        }
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun startShimmer() {
        shimmerLoading.visibility = View.VISIBLE
        shimmerLoading.startShimmer()
    }

    private fun stopShimmer() {
        shimmerLoading.visibility = View.GONE
        shimmerLoading.stopShimmer()
    }

    private fun dataAvailable() {
        layoutDataAvailable.visibility = View.VISIBLE
        layoutDataUnavailable.visibility = View.GONE
    }

    private fun dataUnavailable() {

    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            it?.let {
                viewModel.getWeatherData(it.latitude.toString(), it.longitude.toString())
            } ?: kotlin.run {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        enableMyLocation()
    }

    private fun isPermissionGranted() : Boolean {
        return !(ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED)
    }
}