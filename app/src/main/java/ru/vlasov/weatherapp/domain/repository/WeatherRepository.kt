package ru.vlasov.weatherapp.domain.repository

import ru.vlasov.weatherapp.data.remote.model.ForecastResponseDto
import ru.vlasov.weatherapp.domain.model.ForecastResponse
import ru.vlasov.weatherapp.domain.model.Weather
import ru.vlasov.weatherapp.domain.util.Resource

interface WeatherRepository {

    suspend fun getWeatherGeoCoordinates(lat: Double, lon: Double): Resource<Weather>

    suspend fun getWeatherCity(searchQueryCity: String): Resource<Weather>

    suspend fun getWeatherWithGps(): Resource<Weather>
}