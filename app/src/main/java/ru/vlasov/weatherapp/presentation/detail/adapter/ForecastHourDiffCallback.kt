package ru.vlasov.weatherapp.presentation.detail.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.vlasov.weatherapp.domain.model.ForecastDayItem
import ru.vlasov.weatherapp.domain.model.ForecastListItem

class ForecastHourDiffCallback: DiffUtil.ItemCallback<ForecastListItem>() {

    override fun areItemsTheSame(oldItem: ForecastListItem, newItem: ForecastListItem) =
        oldItem.dt == newItem.dt

    override fun areContentsTheSame(oldItem: ForecastListItem, newItem: ForecastListItem) =
        oldItem == newItem
}