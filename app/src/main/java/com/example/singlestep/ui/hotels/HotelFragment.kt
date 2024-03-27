package com.example.singlestep.ui.hotels

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.singlestep.R
import com.example.singlestep.databinding.FragmentHotelBinding
import com.example.singlestep.model.Flight
import com.example.singlestep.model.Hotel
import com.example.singlestep.model.TripParameters
import com.example.singlestep.model.TripSummary
import com.example.singlestep.ui.common.adapters.HotelAdapter
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.hideBottomNavigationBar
import com.example.singlestep.utils.showBottomNavigationBar

class HotelFragment : Fragment() {

    private val viewModel: HotelViewModel by viewModels()
    private lateinit var binding: FragmentHotelBinding
    private lateinit var hotelAdapter: HotelAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHotelBinding.inflate(inflater, container, false)
        setupObservers()
        arguments?.let { bundle ->
            val args = HotelFragmentArgs.fromBundle(bundle)
            val tripParameters = args.tripParameters
            val flight = args.flight
            val airlineName = args.airlineName
            val airlineICAOCode = args.airlineICAOCode
            setupViews(tripParameters, flight, airlineName, airlineICAOCode)
        }
        return binding.root
    }

    private fun setupObservers() {
        viewModel.hotelList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onHotelsLoading()
                is Result.Failure -> onHotelsLoadingFailure(result)
                is Result.Success -> onHotelsLoadingSuccess(result.value)
            }
        }
    }

    private fun setupViews(
        tripParameters: TripParameters,
        flight: Flight,
        airlineName: String,
        airlineICAOCode: String
    ) {
        with(binding) {
            hotelAdapter = HotelAdapter(
                tripParameters.checkInDate,
                tripParameters.checkOutDate,
                tripParameters.guests
            ) { hotel ->
                Log.d("HotelFragment", "Selected Hotel: ${hotel.displayName.text}")
                val action = HotelFragmentDirections.actionHotelFragmentToSummaryFragment(
                    TripSummary(
                        tripParameters = tripParameters,
                        hotel = hotel,
                        flight = flight,
                        airlineName = airlineName,
                        airlineICAOCode = airlineICAOCode,
                        itinerarySummary = ""
                    )
                )
                findNavController().navigate(action)
            }
            hotelsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            hotelsRecyclerView.adapter = hotelAdapter

            remainingBudgetTextview.text = getString(
                R.string.remaining_budget,
                "CAD",
                tripParameters.remainingBudget.toInt().toString()
            )

            backButton.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private fun onHotelsLoading() {
        Log.d("HotelFragment", "Loading hotels")
        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.failedHotelsLayout.visibility = View.GONE
    }

    private fun onHotelsLoadingSuccess(hotels: List<Hotel>) {
        Log.d("HotelFragment", "Hotels loaded successfully")
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        hotelAdapter.submitList(hotels)
        if (hotels.isEmpty()) {
            binding.failedHotelsLayout.visibility = View.VISIBLE
            binding.hotelsErrorTextView.text = getString(R.string.hotels_empty_response)
        } else {
            binding.failedHotelsLayout.visibility = View.GONE
        }
    }

    private fun onHotelsLoadingFailure(result: Result.Failure) {
        Log.e("HotelFragment", "Error loading hotels", result.throwable)
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.failedHotelsLayout.visibility = View.VISIBLE
        binding.hotelsErrorTextView.text = getString(R.string.hotels_failed_response)
        checkIfConnectionRestored()
    }

    private fun checkIfConnectionRestored() {
        if (hotelAdapter.itemCount == 0) {
            val connectivityManager =
                requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    viewModel.getHotels()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigationBar(activity)
    }

    override fun onStop() {
        super.onStop()
        showBottomNavigationBar(activity)
    }
}
