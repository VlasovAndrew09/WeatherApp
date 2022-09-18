package ru.vlasov.weatherapp.presentation.home.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.vlasov.weatherapp.domain.model.ForecastDayItem

class ForecastDayDiffCallback: DiffUtil.ItemCallback<ForecastDayItem>() {

    override fun areItemsTheSame(oldItem: ForecastDayItem, newItem: ForecastDayItem) =
        oldItem.day == newItem.day

    override fun areContentsTheSame(oldItem: ForecastDayItem, newItem: ForecastDayItem) =
        oldItem == newItem
}