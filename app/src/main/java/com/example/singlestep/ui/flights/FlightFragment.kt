package com.example.singlestep.ui.flights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.singlestep.databinding.FragmentFlightBinding
import com.example.singlestep.model.FlightInfo
import com.example.singlestep.model.TripParameters
import com.example.singlestep.ui.common.adapters.FlightAdapter
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.onLoading
import com.example.singlestep.utils.onLoadingFailure


class FlightFragment : Fragment() {

    private val viewModel: FlightViewModel by viewModels()
    private lateinit var binding: FragmentFlightBinding
    private lateinit var flightAdapter: FlightAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlightBinding.inflate(inflater, container, false)
        setupObservers()
        arguments?.let { bundle ->
            // THIS IS WHERE DISPLAY IS CHANGED ( in setupViews )
            setupViews(FlightFragmentArgs.fromBundle(bundle).tripParameters)
        }
        return binding.root
    }

    private fun setupObservers() {
        viewModel.flightList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onLoading()
                is Result.Failure -> onLoadingFailure()
                is Result.Success -> onFlightLoadingSuccess(result.value)
            }
        }
    }

    private fun setupViews(tripParameters: TripParameters) {
        with(binding) {
            flightAdapter = FlightAdapter { flightInfo ->
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

    private fun onFlightLoadingSuccess(attractions: List<FlightInfo>) {
        flightAdapter.submitList(attractions)
    }
}