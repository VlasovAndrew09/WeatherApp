package ru.vlasov.weatherapp.presentation.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import ru.vlasov.weatherapp.R
import ru.vlasov.weatherapp.databinding.FragmentHomeBinding
import ru.vlasov.weatherapp.presentation.home.adapter.ForecastDayListAdapter
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()
    val args: HomeFragmentArgs by navArgs()

    @Inject
    lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentHomeBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissions()

        val adapter = ForecastDayListAdapter()
        adapter.onItemClickListener = {
            val bundle = Bundle().apply {
                putParcelable("forecastDayItem", it)
                putString("cityName", viewModel.getCityName())
            }
            findNavController().navigate(
                R.id.action_navigation_home_to_weatherDetailFragment,
                bundle
            )
        }
        binding.rvForecast.adapter = adapter

        viewModel.weatherForecast.observe(viewLifecycleOwner) { weatherForecast ->
            with(weatherForecast) {
                with(binding.containerCardWeather) {
                    tvCity.text = viewModel.getCityName()
                    txDateTime.text = getString(R.string.now)
                    tvTemperature.text = currentWeatherResponse.main.temp
                    tvWeatherDescription.text = currentWeatherResponse.weather[0].description
                    tvWindSpeed.text = currentWeatherResponse.wind.speed
                    tvPressure.text = currentWeatherResponse.main.pressure
                    tvHumidity.text = currentWeatherResponse.main.humidity
                    Glide.with(this@HomeFragment)
                        .load(currentWeatherResponse.weather[0].icon)
                        .into(ivWeatherIcon)
                    adapter.submitList(forecastResponse.listDays)
                }
            }
        }
        viewModel.showLoading.observe(viewLifecycleOwner) { showLoading ->
            when (showLoading) {
                true -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                false -> {
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }
        viewModel.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
            when (isEmpty) {
                true -> {
                    binding.containerCardWeather.cardWeather.visibility = View.INVISIBLE
                    binding.tvForecast5Days.visibility = View.INVISIBLE
                    binding.rvForecast.visibility = View.INVISIBLE
                }
                false -> {
                    binding.containerCardWeather.cardWeather.visibility = View.VISIBLE
                    binding.tvForecast5Days.visibility = View.VISIBLE
                    binding.rvForecast.visibility = View.VISIBLE
                }
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            showAlertDialog(errorMessage)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_weather, menu)

        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.getWeatherForecastCity(it)
                    searchView.onActionViewCollapsed()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.action_gps -> {
                if (checkSelfPermissions()) {
                    viewModel.getWeatherForecastGps()
                    return true
                } else {
                    showAlertDialogRequestPermissions()
                    return true
                }
            }
            R.id.action_map -> {
                findNavController().navigate(
                    R.id.action_navigation_home_to_mapFragment
                )
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun requestPermissions() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
        }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

    private fun showAlertDialog(message: String) {
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
                .setNegativeButton(R.string.close, null)
            builder.create().show()
        }
    }

    private fun showAlertDialogRequestPermissions() {
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.not_have_location_permission))
                .setNegativeButton(getString(R.string.no), null)
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            builder.create().show()
        }
    }

    private fun checkSelfPermissions(): Boolean {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return hasAccessCoarseLocationPermission && hasAccessFineLocationPermission
    }
}