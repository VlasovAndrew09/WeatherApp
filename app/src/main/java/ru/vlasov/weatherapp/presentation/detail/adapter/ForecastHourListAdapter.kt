package ru.vlasov.weatherapp.presentation.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import ru.vlasov.weatherapp.databinding.ItemForecastHourBinding
import ru.vlasov.weatherapp.domain.model.ForecastListItem

class ForecastHourListAdapter(private val context: Context) :
    ListAdapter<ForecastListItem, ForecastHourViewHolder>(ForecastHourDiffCallback()) {

    var onItemClickListener: ((ForecastListItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHourViewHolder {
        val binding = ItemForecastHourBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ForecastHourViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastHourViewHolder, position: Int) {
        val forecastHour = getItem(position)
        with(holder.binding) {
            tvTime.text = forecastHour.time
            tvTemp.text = forecastHour.main.temp
            Glide.with(context).load(forecastHour.weather[0].icon).into(ivWeatherIcon)

            root.setOnClickListener {
                onItemClickListener?.let { it(forecastHour) }
            }
        }
    }
}