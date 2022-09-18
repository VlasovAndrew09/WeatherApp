package ru.vlasov.weatherapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wind(
    val speed: String,
    val deg: String,
    val gust: String
): Parcelable
