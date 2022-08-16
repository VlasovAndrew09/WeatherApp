package ru.vlasov.weatherapp.domain

import ru.vlasov.weatherapp.data.remote.model.ForecastResponseDto
import ru.vlasov.weatherapp.domain.util.Resource

interface WeatherRepository {

    suspend fun getForecastWeatherGeoCoordinates(lat: Double, long: Double): Resource<ForecastResponseDto>

    suspend fun getForecastWeatherCity(q: String): Resource<ForecastResponseDto>
}