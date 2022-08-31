package ru.vlasov.weatherapp.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val activeNetwork =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

object ApiConstants {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY_QUERY = "appid"
    const val API_KEY_VALUE = "094aa776d64c50d5b9e9043edd4ffd00"
    const val WEATHER_UNIT_QUERY = "units"
    const val WEATHER_UNIT_VALUE = "metric"
    const val LANGUAGE_QUERY = "lang"
    const val LANGUAGE_VALUE = "ru"
    const val IMAGE_URL = "https://openweathermap.org/img/wn/"
}