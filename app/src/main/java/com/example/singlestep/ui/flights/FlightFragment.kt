package com.example.singlestep.ui.flights

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
import com.amadeus.android.domain.resources.FlightOfferSearch
import com.example.singlestep.databinding.FragmentFlightBinding
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
                Result.Loading -> {
                    Log.d("FlightFragment", "Loading flights")
                    binding.shimmerLayout.startShimmer()
                }

                is Result.Failure -> {
                    Log.e("FlightFragment", "Error loading flights", result.throwable)
                }

                is Result.Success -> {
                    Log.d("FlightFragment", "Flights loaded successfully")
                    onFlightLoadingSuccess(result.value)
                }
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

            backButton.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private fun onFlightLoadingSuccess(flights: List<FlightOfferSearch>) {
        binding.shimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        flightAdapter.submitList(flights)
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
