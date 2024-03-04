package com.example.singlestep.ui.summary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.singlestep.databinding.FragmentSummaryBinding
import com.example.singlestep.ui.flights.FlightFragmentDirections
import com.example.singlestep.utils.writeToFile
import com.example.singlestep.viewmodel.SharedViewModel
import java.time.LocalDate
import java.time.LocalDateTime

class SummaryFragment : Fragment() {

    // Using activityViewModels() delegate to share the ViewModel across fragments
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    private var details_content = ""

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
        loadSaveFunction()
    }



    private fun observeSelectedFlight() {
        sharedViewModel.selectedFlight.observe(viewLifecycleOwner) { flightInfo ->
            //binding.flightNameTextView.text = "Flight Details"
            binding.airlineCodeTextView.text = "Airline Code: ${flightInfo.details.airlineCode}"
//            todo: to be made into a Model for trip info
            details_content += "${flightInfo.details.airlineCode}\n"
            binding.sourceTextView.text = "Source: ${flightInfo.details.source}"
            details_content += "${flightInfo.details.source}\n"
            binding.priceTextView.text = "Price: ${flightInfo.prices.currency} ${flightInfo.prices.totalCost}"
            details_content += "${flightInfo.prices.currency} ${flightInfo.prices.totalCost}\n"
        }
    }
    private fun displayHotelDetails() {
        val args = SummaryFragmentArgs.fromBundle(requireArguments())
        // Update UI with hotel details
        binding.hotelNameTextView.text = "Hotel Name: ${args.hotelName}"
        details_content += "${args.hotelName}\n"
        binding.hotelAddressTextView.text = "Hotel Address: ${args.hotelAddress}"
        details_content += "${args.hotelAddress}\n"
    }

    private fun loadSaveFunction() {
        binding.saveTripButton.setOnClickListener(View.OnClickListener {
//            writeToFile(LocalDateTime.now().toString(), details_content)
//            val action =
//                FlightFragmentDirections.actionFlightFragmentToHotelFragment()
//            findNavController().navigate(action)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
