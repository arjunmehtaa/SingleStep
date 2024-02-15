package com.example.singlestep.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.singlestep.R
import com.example.singlestep.databinding.FragmentSearchBinding
import com.example.singlestep.model.Budget
import com.example.singlestep.model.Location
import com.example.singlestep.model.TripParameters
import com.example.singlestep.ui.common.BudgetAdapter
import com.example.singlestep.ui.common.DestinationAdapter
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.placeToLocation
import com.example.singlestep.utils.setupPlacesAutocompleteFragment
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var destinationAdapter: DestinationAdapter
    private lateinit var budgetAdapter: BudgetAdapter
    private lateinit var startLocation: Location
    private lateinit var destination: Location

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupObservers()
        setupViews()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.destinationList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onLoading()
                is Result.Failure -> onLoadingFailure()
                is Result.Success -> onDestinationLoadingSuccess(result.value)
            }
        }
        viewModel.budgetList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onLoading()
                is Result.Failure -> onLoadingFailure()
                is Result.Success -> onBudgetLoadingSuccess(result.value)
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            destinationAdapter = DestinationAdapter()
            popularDestinationsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            popularDestinationsRecyclerView.adapter = destinationAdapter

            budgetAdapter = BudgetAdapter {
                budgetEditText.setText(it.value.toString())
                Toast.makeText(
                    root.context,
                    "Autofilled budget from suggestions",
                    Toast.LENGTH_SHORT
                ).show()
            }
            suggestedBudgetsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            suggestedBudgetsRecyclerView.adapter = budgetAdapter

            datePickerEditText.setOnClickListener {
                datePickerDialog()
            }

            budgetEditText.addTextChangedListener(budgetTextWatcher)

            searchButton.setOnClickListener {
                val tripParameters = TripParameters(
                    startLocation,
                    destination,
                    datePickerEditText.text.toString(),
                    budgetEditText.text.toString(),
                    guestsEditText.text.toString()
                )
                val action =
                    SearchFragmentDirections.actionSearchFragmentToDetailFragment(tripParameters)
                findNavController(requireView()).navigate(action)
            }

            val startLocationFragment =
                childFragmentManager.findFragmentById(R.id.start_location_fragment) as AutocompleteSupportFragment
            setupPlacesAutocompleteFragment(
                root.context,
                startLocationFragment,
                "Enter start location",
                R.drawable.icon_start_location,
                R.drawable.background_border_rounded_top,
            ) { place -> startLocation = placeToLocation(place) }

            val destinationFragment =
                childFragmentManager.findFragmentById(R.id.destination_fragment) as AutocompleteSupportFragment
            setupPlacesAutocompleteFragment(
                root.context,
                destinationFragment,
                "Enter destination",
                R.drawable.icon_destination,
                R.drawable.background_border
            ) { place -> destination = placeToLocation(place) }
        }
    }

    private fun datePickerDialog() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTitleText("Select a date range")
        val datePicker = builder.build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDateString = sdf.format(Date(startDate))
            val endDateString = sdf.format(Date(endDate))
            val selectedDateRange = "$startDateString - $endDateString"
            binding.datePickerEditText.setText(selectedDateRange)
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }


    private fun onLoading() {
        Log.i("Loading", "onLoading()")
    }

    private fun onLoadingFailure() {
        Log.i("Loading Failed", "onLoadingFailure()")
    }

    private fun onDestinationLoadingSuccess(destinations: List<Location>) {
        destinationAdapter.submitList(destinations)
    }

    private fun onBudgetLoadingSuccess(budgets: List<Budget>) {
        budgetAdapter.submitList(budgets)
    }

    private val budgetTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val budgetEditText = binding.budgetEditText
            budgetEditText.removeTextChangedListener(this)
            val text = budgetEditText.text
            if (text.isNotEmpty()) {
                if (text.toString() == "$") {
                    budgetEditText.setText("")
                } else {
                    val regex = Regex("[^0-9]")
                    val value = regex.replace(text.toString(), "").toDouble()
                    val budgetString = NumberFormat.getInstance(Locale.CANADA).format(value)
                    budgetEditText.setText(getString(R.string.budget_text, budgetString))
                    budgetEditText.setSelection(budgetEditText.text.length)
                }
            }
            budgetEditText.addTextChangedListener(this)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}