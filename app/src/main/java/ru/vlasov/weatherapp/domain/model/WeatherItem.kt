package ru.vlasov.weatherapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherItem(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
): Parcelable

