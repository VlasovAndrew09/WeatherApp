package ru.vlasov.weatherapp.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.vlasov.weatherapp.R
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
    @ApplicationContext private val context: Context,
    private val locationTracker: LocationTracker
) : WeatherRepository {

    override suspend fun getWeatherGeoCoordinates(
        lat: Double,
        lon: Double
    ): Resource<Weather> {
        if (context.isInternetAvailable().not()) {
            return Resource.Error(context.getString(R.string.check_network_connection))
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
            Resource.Error(e.message ?: context.getString(R.string.unknown_error_occurred))
        }
    }

    override suspend fun getWeatherCity(searchQueryCity: String): Resource<Weather> {
        if (context.isInternetAvailable().not()) {
            return Resource.Error(context.getString(R.string.check_network_connection))
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
            Resource.Error(e.message ?: context.getString(R.string.unknown_error_occurred))
        }
    }

    override suspend fun getWeatherWithGps(): Resource<Weather> {
        if (context.isInternetAvailable().not()) {
            return Resource.Error(context.getString(R.string.check_network_connection))
        }
        if (locationTracker.checkGpsEnabled().not()) {
            return Resource.Error(context.getString(R.string.enable_geolocation))
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
                Resource.Error(e.message ?: context.getString(R.string.unknown_error_occurred))
            }
        } ?: Resource.Error(context.getString(R.string.failed_to_get_location))
    }

}