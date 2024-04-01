package com.example.singlestep.ui.explore

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amadeus.android.domain.resources.Activity
import com.example.singlestep.R
import com.example.singlestep.databinding.FragmentExploreBinding
import com.example.singlestep.ui.common.adapters.TouristAttractionAdapter
import com.example.singlestep.utils.Result
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class ExploreFragment : Fragment() {

    private val viewModel: ExploreViewModel by viewModels()
    private lateinit var binding: FragmentExploreBinding
    private lateinit var touristAttractionAdapter: TouristAttractionAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        setupObservers()
        setupViews()
        handleLocationPermission()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.touristAttractionList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onAttractionsLoading()
                is Result.Failure -> onAttractionsLoadingFailure()
                is Result.Success -> onAttractionsLoadingSuccess(result.value)
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            touristAttractionAdapter = TouristAttractionAdapter {
                touristAttractionsRecyclerView.scrollToPosition(it)
            }
            touristAttractionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            touristAttractionsRecyclerView.adapter = touristAttractionAdapter
        }
    }

    private fun onAttractionsLoading() {
        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.failedAttractionsLayout.visibility = View.GONE
    }

    private fun onAttractionsLoadingSuccess(attractions: List<Activity>) {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        touristAttractionAdapter.submitList(attractions)
        if (attractions.isEmpty()) {
            binding.failedAttractionsLayout.visibility = View.VISIBLE
            binding.exploreErrorTextView.text = getString(R.string.explore_empty_response)
        } else {
            binding.failedAttractionsLayout.visibility = View.GONE
        }
    }

    private fun onAttractionsLoadingFailure() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.failedAttractionsLayout.visibility = View.VISIBLE
        binding.exploreErrorTextView.text = getString(R.string.explore_failed_response)
        checkIfConnectionRestored()
    }

    private fun checkIfConnectionRestored() {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                handleLocationPermission()
            }
        })
    }

    private fun handleLocationPermission() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Check if the required permissions are granted, if not, request them
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.failedLocationLayout.visibility = View.VISIBLE
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        binding.failedLocationLayout.visibility = View.GONE

        // Permissions are granted, proceed to get the location
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                if (touristAttractionAdapter.itemCount == 0) {
                    viewModel.getTouristAttractionList(latitude, longitude)
                }
            } else {
                // emulator always provides null location, so send it waterloo's coordinates by default
                viewModel.getTouristAttractionList(43.466667, -80.516670)
            }
        }.addOnFailureListener { e ->
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleLocationPermission()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }
}