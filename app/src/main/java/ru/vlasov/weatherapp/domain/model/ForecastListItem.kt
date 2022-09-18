package ru.vlasov.weatherapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForecastListItem(
    val dt: Long,
    val main: WeatherMain,
    val weather: List<WeatherItem>,
    val wind: Wind,
    val time: String
): Parcelable
