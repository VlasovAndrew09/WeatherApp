package ru.vlasov.weatherapp.data.remote.model

import com.faskn.app.weatherapp.domain.model.WeatherItemDto
import com.faskn.app.weatherapp.domain.model.WeatherMainDto
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponseDto(

    @SerializedName("cod")
    val cod: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("main")
    val main: WeatherMainDto,

    @SerializedName("weather")
    val weather: List<WeatherItemDto>,

    @SerializedName("wind")
    val wind: WindDto
)
