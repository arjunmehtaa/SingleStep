package com.example.singlestep.ui.flights

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amadeus.android.domain.resources.FlightOfferSearch
import com.example.singlestep.databinding.FragmentFlightBinding
import com.example.singlestep.model.TripParameters
import com.example.singlestep.ui.common.adapters.FlightAdapter
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.hideBottomNavigationBar
import com.example.singlestep.utils.onLoading
import com.example.singlestep.utils.onLoadingFailure
import com.example.singlestep.utils.showBottomNavigationBar
import com.example.singlestep.utils.toFlightInfo // Ensure this util function is correctly defined in your utilities package
import com.example.singlestep.viewmodel.SharedViewModel

class FlightFragment : Fragment() {

    private val viewModel: FlightViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentFlightBinding
    private lateinit var flightAdapter: FlightAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
    }

    private fun setupViews(tripParameters: TripParameters) {
        with(binding) {
            flightAdapter = FlightAdapter (requireContext()) { selectedFlight ->
                // Note: Since the navigation to SummaryFragment is now handled in HotelFragment,
                // you don't need to navigate to SummaryFragment from here.
                // You can update the sharedViewModel with the selected flight info or perform other actions as needed.
                val flightInfo = selectedFlight.toFlightInfo()
                sharedViewModel.selectFlight(flightInfo)
                // Navigate to the next step in the flow, which could be showing details for the selected flight or going to HotelFragment
                val action =
                    FlightFragmentDirections.actionFlightFragmentToHotelFragment(tripParameters)
                findNavController().navigate(action)
            }
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
