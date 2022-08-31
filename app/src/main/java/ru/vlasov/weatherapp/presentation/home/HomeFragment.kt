package ru.vlasov.weatherapp.presentation.home

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import ru.vlasov.weatherapp.R
import ru.vlasov.weatherapp.databinding.FragmentHomeBinding
import ru.vlasov.weatherapp.domain.util.Resource
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
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

        val coord = args.coord
        coord?.let {
            viewModel.getForecastWeatherGeoCoordinates(coord.lat, coord.lon)
        }

        viewModel.weatherForecast.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.let { data ->
                        binding.tvCity.text = data.city.name
                        binding.txDateTime.text = data.list[0].dtTxt
                        binding.tvTemperature.text = data.list[0].main.temp
                        binding.tvWeatherDescription.text = data.list[0].weather[0].description
                        binding.tvWindSpeed.text = data.list[0].wind.speed
                        binding.tvPressure.text = data.list[0].main.pressure
                        binding.tvHumidity.text = data.list[0].main.humidity
                        Glide.with(this).load(data.list[0].weather[0].icon).into(binding.ivWeatherIcon)
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
                is Resource.DisabledInternet -> {
                    binding.progressBar.visibility = View.GONE
                    showAlertDialog("Проверьте подключение к сети и повторите попытку.")
                }
                is Resource.DisabledGps -> {
                    binding.progressBar.visibility = View.GONE
                    showAlertDialog("Не удалось получить местоположение. Включите на устройстве геолокацию.")
                }
                is Resource.RequestPermissions -> {
                    binding.progressBar.visibility = View.GONE
                    showAlertDialogRequestPermissions()
                }
            }
        }
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

        /*val gpsView = menu.findItem(R.id.action_gps)?.actionView as AppCompatImageButton
        gpsView.setOnClickListener {
            viewModel.getWeatherForecastGps()
        }

        val mapView = menu.findItem(R.id.action_map)?.actionView
        if (mapView != null) {
            mapView.setOnClickListener {
                findNavController().navigate(
                    R.id.action_navigation_home_to_mapFragment
                )
            }
        }*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.action_gps -> {
                viewModel.getWeatherForecastGps()
                return true
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
}