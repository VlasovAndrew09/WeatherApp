package ru.vlasov.weatherapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class ForecastResponseDto(
    @SerializedName("city")
    val city: CityDto?,

    @SerializedName("cnt")
    val cnt: Int?,

    @SerializedName("cod")
    val cod: String?,

    @SerializedName("message")
    val message: Double?,

    @SerializedName("list")
    val list: List<ForecastListItemDto>?
)
