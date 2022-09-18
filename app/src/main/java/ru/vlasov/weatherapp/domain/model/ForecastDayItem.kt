package ru.vlasov.weatherapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForecastDayItem(
    val day: String,
    val dayOfWeek: String,
    val tempMin: String,
    val tempMax: String,
    val list: List<ForecastListItem>
): Parcelable