package com.faskn.app.weatherapp.domain.model

import com.google.gson.annotations.SerializedName

data class WeatherMainDto(

    @SerializedName("temp")
    val temp: Double,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("pressure")
    val pressure: Int
)
