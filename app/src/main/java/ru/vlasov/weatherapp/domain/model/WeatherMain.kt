package ru.vlasov.weatherapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherMain(
    val temp: String,
    val humidity: String,
    val pressure: String
): Parcelable
