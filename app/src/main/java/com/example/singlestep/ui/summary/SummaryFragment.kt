package com.example.singlestep.ui.summary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.singlestep.databinding.FragmentSummaryBinding
import com.example.singlestep.viewmodel.SharedViewModel

class SummaryFragment : Fragment() {

    // Using activityViewModels() delegate to share the ViewModel across fragments
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSelectedFlight()
        displayHotelDetails()
    }


    private fun observeSelectedFlight() {
        // Observe the selected flight information from the shared ViewModel
        sharedViewModel.selectedFlight.observe(viewLifecycleOwner) { flightInfo ->
            // Constructing a summary string from various properties
            binding.flightNameTextView.text = "Flight Details"
            binding.airlineCodeTextView.text = "Airline Code: ${flightInfo.details.airlineCode}"
            binding.sourceTextView.text = "Source: ${flightInfo.details.source}"
            binding.priceTextView.text = "Price: ${flightInfo.prices.currency} ${flightInfo.prices.totalCost}"
        }
    }
    private fun displayHotelDetails() {
        val args = SummaryFragmentArgs.fromBundle(requireArguments())
        // Update UI with hotel details
        binding.hotelNameTextView.text = "Hotel Name: ${args.hotelName}"
        binding.hotelAddressTextView.text = "Hotel Address: ${args.hotelAddress}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
