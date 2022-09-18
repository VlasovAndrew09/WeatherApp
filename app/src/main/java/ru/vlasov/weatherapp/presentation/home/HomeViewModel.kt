package ru.vlasov.weatherapp.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vlasov.weatherapp.domain.model.Coord
import ru.vlasov.weatherapp.domain.repository.WeatherRepository
import ru.vlasov.weatherapp.domain.model.Weather
import ru.vlasov.weatherapp.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel() {

    private val _weatherForecast = MutableLiveData<Resource<Weather>>()
    val weatherForecast = _weatherForecast
    val coordFromArgs = MutableLiveData<Coord?>()

    private val _uiState: MutableStateFlow<Coord?> = MutableStateFlow(null)
    val uiState: StateFlow<Coord?> = _uiState

    init {
        _weatherForecast.value = Resource.Empty()
    }

    fun getWeatherForecastCity(searchQueryCity: String) {
        viewModelScope.launch {
            _weatherForecast.value = Resource.Loading()
            _weatherForecast.value = repository.getWeatherCity(searchQueryCity)
        }
    }

    fun getWeatherForecastGps() {
        viewModelScope.launch {
            _weatherForecast.value = Resource.Loading()
            _weatherForecast.value = repository.getWeatherWithGps()
        }
    }

    fun getForecastWeatherGeoCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            _weatherForecast.value = Resource.Loading()
            _weatherForecast.value = repository.getWeatherGeoCoordinates(lat, lon)
        }
    }

    fun getCityName(): String? {
        return weatherForecast.value?.data?.let { data ->
            if (data.currentWeatherResponse.name != "") {
                data.currentWeatherResponse.name
            } else {
                val coord = data.currentWeatherResponse.coord
                "${coord.lat}, ${coord.lon}"
            }
        }
    }

    fun setCoordFromArgs(coord: Coord) {
        coordFromArgs.value = coord
    }

    fun set_uiStatenull() {
        _uiState.value = null
    }
}