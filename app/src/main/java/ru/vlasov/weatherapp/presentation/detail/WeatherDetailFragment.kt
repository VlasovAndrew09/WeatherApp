package ru.vlasov.weatherapp.presentation.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.vlasov.weatherapp.R
import ru.vlasov.weatherapp.databinding.FragmentWeatherDetailBinding
import ru.vlasov.weatherapp.presentation.detail.adapter.ForecastHourListAdapter

@AndroidEntryPoint
class WeatherDetailFragment : Fragment() {

    private val viewModel: WeatherDetailViewModel by viewModels()
    val args: WeatherDetailFragmentArgs by navArgs()

    private var _binding: FragmentWeatherDetailBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentWeatherDetailBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val forecastDayItem = args.forecastDayItem
        viewModel.weatherForecastDay.value = forecastDayItem
        viewModel.selectedHourItem.value = forecastDayItem.list[0]

        val adapter = ForecastHourListAdapter(requireContext())
        adapter.onItemClickListener = {
            viewModel.selectedHourItem.value = it
        }
        binding.rvWeatherHour.adapter = adapter

        viewModel.weatherForecastDay.observe(viewLifecycleOwner) { weatherForecastDay ->
            adapter.submitList(weatherForecastDay.list)
            with(binding.containerCardWeather) {
                tvCity.text = getString(R.string.cityNameAndDay, args.cityName, weatherForecastDay.day)
            }
        }

        viewModel.selectedHourItem.observe(viewLifecycleOwner) { selectedHourItem ->
            with(binding.containerCardWeather) {
                txDateTime.text = selectedHourItem.time
                tvTemperature.text = selectedHourItem.main.temp
                tvWeatherDescription.text = selectedHourItem.weather[0].description
                tvWindSpeed.text = selectedHourItem.wind.speed
                tvPressure.text = selectedHourItem.main.pressure
                tvHumidity.text = selectedHourItem.main.humidity
                Glide.with(this@WeatherDetailFragment).load(selectedHourItem.weather[0].icon)
                    .into(ivWeatherIcon)
            }
        }
    }
}