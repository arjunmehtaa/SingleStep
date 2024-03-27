package com.example.singlestep.ui.flights

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
import com.amadeus.android.domain.resources.Airline
import com.example.singlestep.R
import com.example.singlestep.databinding.FragmentFlightBinding
import com.example.singlestep.model.Flight
import com.example.singlestep.model.TripParameters
import com.example.singlestep.ui.common.adapters.FlightAdapter
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.hideBottomNavigationBar
import com.example.singlestep.utils.onLoading
import com.example.singlestep.utils.onLoadingFailure
import com.example.singlestep.utils.showBottomNavigationBar

class FlightFragment : Fragment() {

    private val viewModel: FlightViewModel by viewModels()
    private lateinit var binding: FragmentFlightBinding
    private lateinit var flightAdapter: FlightAdapter
    private lateinit var airlineNamesMap: HashMap<String, Airline>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlightBinding.inflate(inflater, container, false)
        setupObservers()
        arguments?.let { bundle ->
            val tripParameters = FlightFragmentArgs.fromBundle(bundle).tripParameters
            setupViews(tripParameters)
        }
        return binding.root
    }

    private fun setupObservers() {
        viewModel.flightList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onFlightsLoading()
                is Result.Failure -> onFlightsLoadingFailure(result)
                is Result.Success -> onFlightsLoadingSuccess(result.value)
            }

        }

        viewModel.airlineNamesMap.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onLoading()
                is Result.Failure -> onLoadingFailure()
                is Result.Success -> {
                    airlineNamesMap = result.value
                }
            }
        }
    }

    private fun setupViews(tripParameters: TripParameters) {
        with(binding) {
            flightAdapter = FlightAdapter(
                tripParameters.source.city,
                tripParameters.destination.city,
                tripParameters.guests,
                airlineNameGetter = {
                    airlineNamesMap[it]?.businessName
                },
                airlineICAOCodeGetter = {
                    airlineNamesMap[it]?.icaoCode
                },
                clickListener = { flight, airlineName, airlineICAOCode ->
                    tripParameters.remainingBudget =
                        tripParameters.originalBudget.minus(flight.rawPrice)
                    val action = FlightFragmentDirections.actionFlightFragmentToHotelFragment(
                        tripParameters = tripParameters,
                        flight = flight,
                        airlineName = airlineName.orEmpty(),
                        airlineICAOCode = airlineICAOCode.orEmpty()
                    )
                    findNavController().navigate(action)
                }
            )
            flightRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            flightRecyclerView.adapter = flightAdapter

            remainingBudgetTextview.text = getString(
                R.string.remaining_budget,
                "CAD",
                tripParameters.originalBudget.toInt().toString()
            )

            backButton.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private fun onFlightsLoading() {
        Log.d("FlightFragment", "Loading flights")
        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.failedFlightsLayout.visibility = View.GONE
    }

    private fun onFlightsLoadingSuccess(flights: List<Flight>) {
        Log.d("FlightFragment", "Flights loaded successfully")
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        flightAdapter.submitList(flights)
        if (flights.isEmpty()) {
            binding.failedFlightsLayout.visibility = View.VISIBLE
            binding.flightsErrorTextView.text = getString(R.string.flights_empty_response)
        } else {
            binding.failedFlightsLayout.visibility = View.GONE
        }
    }

    private fun onFlightsLoadingFailure(result: Result.Failure) {
        Log.e("FlightFragment", "Error loading flights", result.throwable)
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.failedFlightsLayout.visibility = View.VISIBLE
        binding.flightsErrorTextView.text = getString(R.string.flights_failed_response)
        checkIfConnectionRestored()
    }

    private fun checkIfConnectionRestored() {
        if (flightAdapter.itemCount == 0) {
            val connectivityManager =
                requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    viewModel.getFlightAttractionList()
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
