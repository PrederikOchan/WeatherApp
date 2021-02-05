package com.prederikochan.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun getDays(seconds: Int) : String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = seconds*1000L
    val date : Date = calendar.time

    return SimpleDateFormat("EEEE", Locale.ENGLISH).format(date)
}