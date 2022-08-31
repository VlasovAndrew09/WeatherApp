package com.faskn.app.weatherapp.domain.model

import com.google.gson.annotations.SerializedName

data class WeatherItemDto(

    @SerializedName("id")
    val id: Int,

    @SerializedName("main")
    val main: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val icon: String
)
