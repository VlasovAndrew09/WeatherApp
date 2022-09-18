package ru.vlasov.weatherapp.data.remote

import androidx.viewbinding.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.vlasov.weatherapp.data.remote.model.CurrentWeatherResponseDto
import ru.vlasov.weatherapp.data.remote.model.ForecastResponseDto

interface WeatherApi {

    @GET("forecast")
    suspend fun getForecastWeatherGeoCoordinates(
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double
    ): ForecastResponseDto

    @GET("forecast")
    suspend fun getForecastWeatherCity(
        @Query("q") searchQueryCity: String
    ): ForecastResponseDto

    @GET("weather")
    suspend fun getCurrentWeatherGeoCoordinates(
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double
    ): CurrentWeatherResponseDto

    @GET("weather")
    suspend fun getCurrentWeatherCity(
        @Query("q") searchQueryCity: String
    ): CurrentWeatherResponseDto
}