package ru.vlasov.weatherapp.domain.model

data class Weather(
    val currentWeatherResponse: CurrentWeatherResponse,
    val forecastResponse: ForecastResponse
)
