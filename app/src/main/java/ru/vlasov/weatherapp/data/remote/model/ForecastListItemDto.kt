package ru.vlasov.weatherapp.data.remote.model

import com.faskn.app.weatherapp.domain.model.WeatherMainDto
import com.faskn.app.weatherapp.domain.model.WeatherItemDto
import com.google.gson.annotations.SerializedName

data class ForecastListItemDto(
    @SerializedName("dt")
    val dt: Long?,

    @SerializedName("main")
    val main: WeatherMainDto?,

    @SerializedName("weather")
    val weather: List<WeatherItemDto?>?,

    @SerializedName("wind")
    val wind: WindDto?,

    @SerializedName("dt_txt")
    val dtTxt: String?
)
