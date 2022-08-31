package ru.vlasov.weatherapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class WindDto(

    @SerializedName("speed")
    val speed: Double,

    @SerializedName("deg")
    val deg: Double,

    @SerializedName("gust")
    val gust: Double
)
