package ru.vlasov.weatherapp.domain.model

data class ForecastListItem(
    val dt: Long,
    val main: WeatherMain,
    val weather: List<WeatherItem>,
    val wind: Wind,
    val dtTxt: String
)
