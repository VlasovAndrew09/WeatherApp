package ru.vlasov.weatherapp.domain.repository

import ru.vlasov.weatherapp.data.remote.model.ForecastResponseDto
import ru.vlasov.weatherapp.domain.model.ForecastResponse
import ru.vlasov.weatherapp.domain.util.Resource

interface WeatherRepository {

    suspend fun getForecastWeatherGeoCoordinates(lat: Double, lon: Double): Resource<ForecastResponse>

    suspend fun getForecastWeatherCity(searchQueryCity: String): Resource<ForecastResponse>

    suspend fun getForecastWeatherWithGps(): Resource<ForecastResponse>
}