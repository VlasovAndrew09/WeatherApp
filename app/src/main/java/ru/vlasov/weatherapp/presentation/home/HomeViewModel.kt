package ru.vlasov.weatherapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.vlasov.weatherapp.data.remote.model.ForecastResponseDto
import ru.vlasov.weatherapp.domain.WeatherRepository
import ru.vlasov.weatherapp.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel() {

    private val _weatherForecast = MutableLiveData<Resource<ForecastResponseDto>>()
    val weatherForecast = _weatherForecast

    init {
        getWeatherForecast()
    }

    private fun getWeatherForecast() {
        viewModelScope.launch {
            _weatherForecast.value = repository.getForecastWeatherCity("Москва")
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}