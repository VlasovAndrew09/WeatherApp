package ru.vlasov.weatherapp.presentation.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vlasov.weatherapp.domain.model.ForecastDayItem
import ru.vlasov.weatherapp.domain.model.ForecastListItem
import ru.vlasov.weatherapp.domain.model.Weather
import ru.vlasov.weatherapp.domain.util.Resource

class WeatherDetailViewModel : ViewModel() {

    //private val _weatherForecastDay = MutableLiveData<ForecastDayItem>()
    val weatherForecastDay = MutableLiveData<ForecastDayItem>()
    val selectedHourItem = MutableLiveData<ForecastListItem>()

}