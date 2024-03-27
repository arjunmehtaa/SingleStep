package com.example.singlestep.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.singlestep.model.Location
import com.example.singlestep.model.TripParameters
import com.example.singlestep.ui.common.adapters.BudgetAdapter
import com.example.singlestep.ui.common.adapters.DestinationAdapter
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.formatBudget
import com.example.singlestep.utils.getSelectedDateRange
import com.example.singlestep.utils.onLoading
import com.example.singlestep.utils.onLoadingFailure
import com.example.singlestep.utils.placeToLocation
import com.example.singlestep.utils.setupPlacesAutocompleteFragment
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker

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
                is Result.Success -> destinationAdapter.submitList(result.value)
            }
        }
        viewModel.budgetList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onLoading()
                is Result.Failure -> onLoadingFailure()
                is Result.Success -> budgetAdapter.submitList(result.value)
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
                        if (numGuests == 1) {
                            guestsEditText.setText(getString(R.string.num_guests, numGuests))
                        } else {
                            guestsEditText.setText(
                                getString(
                                    R.string.num_guests_multiple,
                                    numGuests
                                )
                            )
                        }
                    }
                    validateFields()
                }
            }

            addGuestButton.setOnClickListener {
                numGuests++
                if (numGuests == 1) {
                    guestsEditText.setText(getString(R.string.num_guests, numGuests))
                } else {
                    guestsEditText.setText(getString(R.string.num_guests_multiple, numGuests))
                }
                validateFields()
            }

            planTripButton.setOnClickListener {
                val dates = datePickerEditText.text.toString().split(" - ")
                if (dates.size == 2) {
                    val tripParameters = TripParameters(
                        source!!,
                        destination!!,
                        dates[0].trim(),
                        dates[1].trim(),
                        budget,
                        budget,
                        numGuests
                    )
                    val action =
                        SearchFragmentDirections.actionSearchFragmentToFlightFragment(tripParameters)
                    findNavController(requireView()).navigate(action)
                } else {
                    Toast.makeText(context, "Please select valid date range", Toast.LENGTH_LONG)
                        .show()
                }
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
        setupPlacesAutocompleteFragment(sourceFragment,
            getString(R.string.enter_start_location),
            placeSelectedListener = {
                source = placeToLocation(it)
                validateFields()
            },
            clearButtonClickedListener = {
                source = null
                validateFields()
            })

        setupPlacesAutocompleteFragment(destinationFragment,
            getString(R.string.enter_destination),
            placeSelectedListener = {
                destination = placeToLocation(it)
                validateFields()
            },
            clearButtonClickedListener = {
                destination = null
                validateFields()
            })
    }

    private fun validateFields() {
        binding.planTripButton.isEnabled =
            (numGuests > 0 && budget > 0 && binding.datePickerEditText.text.isNotEmpty() && source != null && destination != null)
    }

    private fun datePickerDialog() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val dateValidator = DateValidatorPointForward.from(System.currentTimeMillis())
        val constraintsBuilder = CalendarConstraints.Builder().setValidator(dateValidator)
        builder.setCalendarConstraints(constraintsBuilder.build())
        builder.setTitleText("Select a date range")
        val datePicker = builder.setTheme(R.style.AppTheme_DatePicker).build()
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
                    val formattedBudget = formatBudget(text.toString())
                    budget = formattedBudget.first
                    budgetEditText.setText(getString(R.string.budget_text, formattedBudget.second))
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