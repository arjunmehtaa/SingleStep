package com.example.singlestep.ui.summary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.singlestep.R
import com.example.singlestep.databinding.FragmentSummaryBinding
import com.example.singlestep.viewmodel.SharedViewModel

class SummaryFragment : Fragment() {

    // Using activityViewModels() delegate to share the ViewModel across fragments
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSelectedFlightInfo()
    }

    private fun observeSelectedFlightInfo() {
        // Observe the selected flight information from the shared ViewModel
        sharedViewModel.selectedFlightInfo.observe(viewLifecycleOwner) { flightInfo ->
            flightInfo?.let {
                // Example of constructing a summary string from various properties
                val summaryText = StringBuilder()
                summaryText.append("Airline Code: ${it.details.airlineCode}\n")
                summaryText.append("Source: ${it.details.source}\n")
                summaryText.append("Price: ${it.prices.currency} ${it.prices.totalCost}")

                // Update the TextView with this summary information
                binding.flightNameTextView.text = summaryText.toString()
            } ?: run {
                // Handle the case where flightInfo is null
                binding.flightNameTextView.text = "No flight information available"
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
