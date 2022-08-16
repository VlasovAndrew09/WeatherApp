package ru.vlasov.weatherapp.data

import ru.vlasov.weatherapp.data.remote.WeatherApi
import ru.vlasov.weatherapp.data.remote.model.ForecastResponseDto
import ru.vlasov.weatherapp.domain.WeatherRepository
import ru.vlasov.weatherapp.domain.util.Resource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
): WeatherRepository {

    override suspend fun getForecastWeatherGeoCoordinates(
        lat: Double,
        long: Double
    ): Resource<ForecastResponseDto> {
        return try {
            Resource.Success(
                data = api.getForecastWeatherGeoCoordinates(
                    lat = lat,
                    lon = long
                )
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun getForecastWeatherCity(q: String): Resource<ForecastResponseDto> {
        return try {
            Resource.Success(
                data = api.getForecastWeatherCity(
                    q = q
                )
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}