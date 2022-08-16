package com.faskn.app.weatherapp.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class WeatherMainDto(

    @SerializedName("temp")
    val temp: Double?,

    @SerializedName("humidity")
    val humidity: Int?
)
