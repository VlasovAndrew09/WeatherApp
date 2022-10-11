package ru.vlasov.weatherapp.data

import android.app.Application
import ru.vlasov.weatherapp.data.mapper.toCurrentWeatherResponse
import ru.vlasov.weatherapp.data.mapper.toForecastResponse
import ru.vlasov.weatherapp.data.remote.WeatherApi
import ru.vlasov.weatherapp.data.remote.isInternetAvailable
import ru.vlasov.weatherapp.domain.location.LocationTracker
import ru.vlasov.weatherapp.domain.repository.WeatherRepository
import ru.vlasov.weatherapp.domain.model.Weather
import ru.vlasov.weatherapp.domain.util.Resource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val application: Application,
    private val locationTracker: LocationTracker
) : WeatherRepository {

    override suspend fun getWeatherGeoCoordinates(
        lat: Double,
        lon: Double
    ): Resource<Weather> {
        if (application.isInternetAvailable().not()) {
            return Resource.Error("Проверьте подключение к сети и повторите попытку.")
        }
        return try {
            Resource.Success(
                data = Weather(
                    api.getCurrentWeatherGeoCoordinates(
                        lat = lat,
                        lon = lon
                    ).toCurrentWeatherResponse(),
                    api.getForecastWeatherGeoCoordinates(
                        lat = lat,
                        lon = lon
                    ).toForecastResponse()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun getWeatherCity(searchQueryCity: String): Resource<Weather> {
        if (application.isInternetAvailable().not()) {
            return Resource.Error("Проверьте подключение к сети и повторите попытку.")
        }
        return try {
            Resource.Success(
                data = Weather(
                    api.getCurrentWeatherCity(
                        searchQueryCity = searchQueryCity
                    ).toCurrentWeatherResponse(),
                    api.getForecastWeatherCity(
                        searchQueryCity = searchQueryCity
                    ).toForecastResponse()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun getWeatherWithGps(): Resource<Weather> {
        if (application.isInternetAvailable().not()) {
            return Resource.Error("Проверьте подключение к сети и повторите попытку.")
        }
        if (locationTracker.checkGpsEnabled().not()) {
            return Resource.Error("Не удалось получить местоположение. Включите на устройстве геолокацию.")
        }
        return locationTracker.getCurrentLocation()?.let {
            try {
                Resource.Success(
                    data = Weather(
                        api.getCurrentWeatherGeoCoordinates(
                            lat = it.latitude,
                            lon = it.longitude
                        ).toCurrentWeatherResponse(),
                        api.getForecastWeatherGeoCoordinates(
                            lat = it.latitude,
                            lon = it.longitude
                        ).toForecastResponse()
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message ?: "An unknown error occurred.")
            }
        } ?: Resource.Error("Не удалось получить местоположение")
    }

}