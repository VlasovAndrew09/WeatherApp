package ru.vlasov.weatherapp.domain.model

data class CurrentWeatherResponse(
    val cod: String,
    val name: String,
    val main: WeatherMain,
    val weather: List<WeatherItem>,
    val wind: Wind
)
