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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.vlasov.weatherapp.R
import ru.vlasov.weatherapp.databinding.FragmentHomeBinding
import ru.vlasov.weatherapp.domain.util.Resource
import ru.vlasov.weatherapp.presentation.home.adapter.ForecastDayListAdapter
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()
    val args: HomeFragmentArgs by navArgs()
    @Inject lateinit var locationProviderClient: FusedLocationProviderClient
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

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
        }
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ))

        /*val coord = args.coord
        args.toBundle().clear()
        coord?.let {
            viewModel.setCoordFromArgs(it)
        }*/

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

        viewModel.weatherForecast.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.let { data ->
                        with(binding.containerCardWeather) {
                            tvCity.text = viewModel.getCityName()
                            txDateTime.text = "Сейчас"
                            tvTemperature.text = data.currentWeatherResponse.main.temp
                            tvWeatherDescription.text =
                                data.currentWeatherResponse.weather[0].description
                            tvWindSpeed.text = data.currentWeatherResponse.wind.speed
                            tvPressure.text = data.currentWeatherResponse.main.pressure
                            tvHumidity.text = data.currentWeatherResponse.main.humidity
                            Glide.with(this@HomeFragment).load(data.currentWeatherResponse.weather[0].icon)
                                .into(ivWeatherIcon)

                            adapter.submitList(data.forecastResponse.listDays)
                        }
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    response.message?.let {
                        showAlertDialog(it)
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Empty -> {

                }
            }
        }
        viewModel.coordFromArgs.observe(viewLifecycleOwner) { coord ->
            if (coord != null) {
                viewModel.getForecastWeatherGeoCoordinates(coord.lat, coord.lon)
                viewModel.coordFromArgs.value = null
            }
        }
        /*lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { coord ->
                    if (coord != null) {
                        viewModel.getForecastWeatherGeoCoordinates(coord.lat, coord.lon)
                        viewModel.set_uiStatenull()
                    }
                }
            }
        }*/
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_weather, menu)

        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.getWeatherForecastCity(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("f", true)
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

    private fun showAlertDialog(message: String) {
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
                .setNegativeButton("Закрыть", null)
            builder.create().show()
        }
    }

    private fun showAlertDialogRequestPermissions() {
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("У приложения нет разрешения для определения местоположения. Открыть настройки?")
                .setNegativeButton("Нет", null)
                .setPositiveButton("Да") { _, _ ->
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