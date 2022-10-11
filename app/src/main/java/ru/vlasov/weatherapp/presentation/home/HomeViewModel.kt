package ru.vlasov.weatherapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vlasov.weatherapp.domain.repository.WeatherRepository
import ru.vlasov.weatherapp.domain.model.Weather
import ru.vlasov.weatherapp.domain.util.Resource
import ru.vlasov.weatherapp.presentation.util.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _state = MutableLiveData<Resource<Weather>>()

    private val _weatherForecast = MutableLiveData<Weather>()
    val weatherForecast: LiveData<Weather>
        get() = _weatherForecast

    private val _errorMessage = SingleLiveEvent<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val _isEmpty = SingleLiveEvent<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty

    init {
        _state.postValue(Resource.Empty())

        _state.observeForever { response ->
            when (response) {
                is Resource.Success -> {
                    _weatherForecast.postValue(response.data!!)
                    _showLoading.postValue(false)
                    _isEmpty.postValue(false)
                }
                is Resource.Error -> {
                    _errorMessage.postValue(response.message!!)
                    _showLoading.postValue(false)
                }
                is Resource.Loading -> {
                    _showLoading.postValue(true)
                }
                is Resource.Empty -> {
                    _isEmpty.postValue(true)
                }
            }
        }
    }

    fun getWeatherForecastCity(searchQueryCity: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(Resource.Loading())
            _state.postValue(repository.getWeatherCity(searchQueryCity))
        }
    }

    fun getWeatherForecastGps() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(Resource.Loading())
            _state.postValue(repository.getWeatherWithGps())
        }
    }

    fun getForecastWeatherGeoCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(Resource.Loading())
            _state.postValue(repository.getWeatherGeoCoordinates(lat, lon))
        }
    }

    fun getCityName(): String? {
        return weatherForecast.value?.let { data ->
            if (data.currentWeatherResponse.name != "") {
                data.currentWeatherResponse.name
            } else {
                val coord = data.currentWeatherResponse.coord
                "${coord.lat}, ${coord.lon}"
            }
        }
    }
}