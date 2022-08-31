package ru.vlasov.weatherapp.data

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.vlasov.weatherapp.data.location.DefaultLocationTracker
import ru.vlasov.weatherapp.data.mapper.toForecastResponse
import ru.vlasov.weatherapp.data.remote.WeatherApi
import ru.vlasov.weatherapp.data.remote.isInternetAvailable
import ru.vlasov.weatherapp.domain.location.LocationTracker
import ru.vlasov.weatherapp.domain.repository.WeatherRepository
import ru.vlasov.weatherapp.domain.model.ForecastResponse
import ru.vlasov.weatherapp.domain.util.Resource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val application: Application,
    private val locationTracker: LocationTracker
) : WeatherRepository {

    override suspend fun getForecastWeatherGeoCoordinates(
        lat: Double,
        lon: Double
    ): Resource<ForecastResponse> {
        return if (application.isInternetAvailable()) {
            return try {
                Resource.Success(
                    data = api.getForecastWeatherGeoCoordinates(
                        lat = lat,
                        lon = lon
                    ).toForecastResponse()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message ?: "An unknown error occurred.")
            }
        } else {
            Resource.DisabledInternet()
        }
    }

    override suspend fun getForecastWeatherCity(searchQueryCity: String): Resource<ForecastResponse> {
        return if (application.isInternetAvailable()) {
            try {
                Resource.Success(
                    data = api.getForecastWeatherCity(
                        searchQueryCity = searchQueryCity
                    ).toForecastResponse()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message ?: "An unknown error occurred.")
            }
        } else {
            Resource.DisabledInternet()
        }
    }

    override suspend fun getForecastWeatherWithGps(): Resource<ForecastResponse> {
        return if (application.isInternetAvailable()) {
            if (locationTracker.checkSelfPermissions()) {
                if (locationTracker.checkGpsEnabled()) {
                    locationTracker.getCurrentLocation()?.let {
                        try {
                            Resource.Success(
                                data = api.getForecastWeatherGeoCoordinates(
                                    lat = it.latitude,
                                    lon = it.longitude
                                ).toForecastResponse()
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Resource.Error(e.message ?: "An unknown error occurred.")
                        }
                    } ?: Resource.Error("Не удалось получить местоположение")
                } else {
                    Resource.DisabledGps()
                }
            } else {
                Resource.RequestPermissions()
            }

        } else {
            Resource.DisabledInternet()
        }
    }

}