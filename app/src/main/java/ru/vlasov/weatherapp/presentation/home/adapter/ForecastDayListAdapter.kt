package ru.vlasov.weatherapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.vlasov.weatherapp.databinding.ItemForecastDayBinding
import ru.vlasov.weatherapp.domain.model.ForecastDayItem

class ForecastDayListAdapter()
    : ListAdapter<ForecastDayItem, ForecastDayViewHolder>(ForecastDayDiffCallback()) {

    var onItemClickListener: ((ForecastDayItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastDayViewHolder {
        val binding = ItemForecastDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ForecastDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastDayViewHolder, position: Int) {
        val forecastDay = getItem(position)
        with(holder.binding) {
            tvDate.text = forecastDay.day
            tvDayOfWeek.text = forecastDay.dayOfWeek
            tvTempMax.text = forecastDay.tempMax
            tvTempMin.text = forecastDay.tempMin

            root.setOnClickListener {
                onItemClickListener?.let{ it(forecastDay) }
            }
        }
    }
}