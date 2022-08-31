package ru.vlasov.weatherapp.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.vlasov.weatherapp.domain.location.LocationTracker
import ru.vlasov.weatherapp.domain.repository.WeatherRepository
import ru.vlasov.weatherapp.domain.model.ForecastResponse
import ru.vlasov.weatherapp.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel() {

    private val _weatherForecast = MutableLiveData<Resource<ForecastResponse>>()
    val weatherForecast = _weatherForecast

    init {
        //getWeatherForecast()
    }

    private fun getWeatherForecast() {
        viewModelScope.launch {
            _weatherForecast.value = repository.getForecastWeatherCity("Москва")
        }
    }

    fun getWeatherForecastCity(searchQueryCity: String) {
        viewModelScope.launch {
            _weatherForecast.value = Resource.Loading()
            _weatherForecast.value = repository.getForecastWeatherCity(searchQueryCity)
        }
    }

    fun getWeatherForecastGps() {
        viewModelScope.launch {
            _weatherForecast.value = Resource.Loading()
            _weatherForecast.value = repository.getForecastWeatherWithGps()
        }
    }

    fun getForecastWeatherGeoCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            _weatherForecast.value = Resource.Loading()
            _weatherForecast.value = repository.getForecastWeatherGeoCoordinates(lat, lon)
        }
    }
}