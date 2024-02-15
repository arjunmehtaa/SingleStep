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
import com.example.singlestep.utils.getSelectedDateRange
import com.example.singlestep.utils.placeToLocation
import com.example.singlestep.utils.setupPlacesAutocompleteFragment
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.NumberFormat
import java.util.Locale

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var destinationAdapter: DestinationAdapter
    private lateinit var budgetAdapter: BudgetAdapter
    private var source: Location? = null
    private var destination: Location? = null
    private var budget: Double = 0.0
    private var numGuests: Int = 0

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
        val sourceFragment =
            childFragmentManager.findFragmentById(R.id.source_fragment) as AutocompleteSupportFragment

        val destinationFragment =
            childFragmentManager.findFragmentById(R.id.destination_fragment) as AutocompleteSupportFragment

        setupLocationFragments(sourceFragment, destinationFragment)

        with(binding) {

            datePickerEditText.setOnClickListener {
                datePickerDialog()
            }

            budgetEditText.addTextChangedListener(budgetTextWatcher)

            removeGuestButton.setOnClickListener {
                if (numGuests > 0) {
                    numGuests--
                    if (numGuests == 0) {
                        guestsEditText.setText("")
                    } else {
                        guestsEditText.setText(getString(R.string.number_of_guests, numGuests))
                    }
                    validateFields()
                }
            }

            addGuestButton.setOnClickListener {
                numGuests++
                guestsEditText.setText(getString(R.string.number_of_guests, numGuests))
                validateFields()
            }

            planTripButton.setOnClickListener {
                val tripParameters = TripParameters(
                    source!!, destination!!, datePickerEditText.text.toString(), budget, numGuests
                )
                val action =
                    SearchFragmentDirections.actionSearchFragmentToDetailFragment(tripParameters)
                findNavController(requireView()).navigate(action)
            }

            destinationAdapter = DestinationAdapter {
                destinationFragment.setText(it.city)
                destination = it
                Toast.makeText(
                    root.context, getString(R.string.autofill_destination), Toast.LENGTH_SHORT
                ).show()
                validateFields()
            }
            popularDestinationsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            popularDestinationsRecyclerView.adapter = destinationAdapter

            budgetAdapter = BudgetAdapter {
                budgetEditText.setText(it.value.toString())
                Toast.makeText(
                    root.context, getString(R.string.autofill_budget), Toast.LENGTH_SHORT
                ).show()
            }
            suggestedBudgetsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            suggestedBudgetsRecyclerView.adapter = budgetAdapter
        }
    }

    private fun setupLocationFragments(
        sourceFragment: AutocompleteSupportFragment,
        destinationFragment: AutocompleteSupportFragment
    ) {
        with(binding) {
            setupPlacesAutocompleteFragment(
                root.context,
                sourceFragment,
                getString(R.string.enter_start_location),
                R.drawable.icon_start_location,
                R.drawable.background_border_rounded_top,
                placeSelectedListener = {
                    source = placeToLocation(it)
                    validateFields()
                },
                clearButtonClickedListener = {
                    source = null
                    validateFields()
                }
            )

            setupPlacesAutocompleteFragment(
                root.context,
                destinationFragment,
                getString(R.string.enter_destination),
                R.drawable.icon_destination,
                R.drawable.background_border,
                placeSelectedListener = {
                    destination = placeToLocation(it)
                    validateFields()
                },
                clearButtonClickedListener = {
                    destination = null
                    validateFields()
                }
            )
        }
    }

    private fun validateFields() {
        binding.planTripButton.isEnabled =
            (numGuests > 0 && budget > 0 && binding.datePickerEditText.text.isNotEmpty() && source != null && destination != null)
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

    private fun datePickerDialog() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTitleText("Select a date range")
        val datePicker = builder.build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDateRange = getSelectedDateRange(selection)
            binding.datePickerEditText.setText(selectedDateRange)
            validateFields()
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }

    private val budgetTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val budgetEditText = binding.budgetEditText
            budgetEditText.removeTextChangedListener(this)
            val text = budgetEditText.text
            if (text.isNotEmpty()) {
                if (text.toString() == "$") {
                    budgetEditText.setText("")
                    budget = 0.0
                } else {
                    val regex = Regex("[^0-9]")
                    val value = regex.replace(text.toString(), "").toDouble()
                    budget = value
                    val budgetString = NumberFormat.getInstance(Locale.CANADA).format(value)
                    budgetEditText.setText(getString(R.string.budget_text, budgetString))
                    budgetEditText.setSelection(budgetEditText.text.length)
                }
                validateFields()
            }
            budgetEditText.addTextChangedListener(this)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}