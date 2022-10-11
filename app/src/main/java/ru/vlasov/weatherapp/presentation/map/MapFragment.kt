package ru.vlasov.weatherapp.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import ru.vlasov.weatherapp.R
import ru.vlasov.weatherapp.databinding.FragmentMapBinding
import ru.vlasov.weatherapp.presentation.home.HomeViewModel

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private val viewModel: HomeViewModel by activityViewModels()
    private var _binding: FragmentMapBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentMapBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onMapReady(googleMap: GoogleMap) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
            googleMap.isMyLocationEnabled = true

        googleMap.setOnMapClickListener {
            val markerOptions = MarkerOptions()
            markerOptions.position(it)
            googleMap.clear()
            googleMap.addMarker(markerOptions)
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Показать погоду для этого места?")
            builder.setPositiveButton("Да") { _, _ ->
                viewModel.getForecastWeatherGeoCoordinates(it.latitude, it.longitude)
                findNavController().popBackStack()
            }
            builder.setNegativeButton("Нет", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}