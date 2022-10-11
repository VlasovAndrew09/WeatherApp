package ru.vlasov.weatherapp.presentation.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vlasov.weatherapp.domain.model.ForecastDayItem
import ru.vlasov.weatherapp.domain.model.ForecastListItem

class WeatherDetailViewModel : ViewModel() {

    val weatherForecastDay = MutableLiveData<ForecastDayItem>()
    val selectedHourItem = MutableLiveData<ForecastListItem>()

}