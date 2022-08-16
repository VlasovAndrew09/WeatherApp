package ru.vlasov.weatherapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class CityDto(

    @SerializedName("id")
    val id: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("country")
    val country: String?,

    @SerializedName("sunrise")
    val sunrise: Long?,

    @SerializedName("sunset")
    val sunset: Long?
)
