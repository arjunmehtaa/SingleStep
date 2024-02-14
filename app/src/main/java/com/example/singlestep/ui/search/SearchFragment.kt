package com.example.singlestep.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.singlestep.databinding.FragmentSearchBinding
import com.example.singlestep.model.Budget
import com.example.singlestep.model.Destination
import com.example.singlestep.model.TripParameters
import com.example.singlestep.ui.common.BudgetAdapter
import com.example.singlestep.ui.common.DestinationAdapter
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.transformIntoDatePicker

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var destinationAdapter: DestinationAdapter
    private lateinit var budgetAdapter: BudgetAdapter

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
                Result.Loading -> onDestinationLoading()
                is Result.Failure -> onDestinationLoadingFailure()
                is Result.Success -> onDestinationLoadingSuccess(result.value)
            }
        }
        viewModel.budgetList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onBudgetLoading()
                is Result.Failure -> onBudgetLoadingFailure()
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

            budgetAdapter = BudgetAdapter()
            searchByBudgetRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            searchByBudgetRecyclerView.adapter = budgetAdapter

            searchButton.setOnClickListener {
                val tripParameters = TripParameters(
                    destinationEditText.text.toString(),
                    datesEditText.text.toString(),
                    budgetEditText.text.toString(),
                    guestsEditText.text.toString()
                )
                val action =
                    SearchFragmentDirections.actionSearchFragmentToDetailFragment(tripParameters)
                findNavController(requireView()).navigate(action)
            }

            datesEditText.transformIntoDatePicker(requireContext(), "MM/dd/yyyy")
        }
    }

    private fun onDestinationLoading() {
        Log.i("Loading", "onDestinationLoading()")
    }

    private fun onDestinationLoadingFailure() {
        Log.i("Loading Failed", "onDestinationLoadingFailure()")
    }

    private fun onDestinationLoadingSuccess(destinations: List<Destination>) {
        destinationAdapter.submitList(destinations)
    }

    private fun onBudgetLoading() {
        Log.i("Loading", "onBudgetLoading()")
    }

    private fun onBudgetLoadingFailure() {
        Log.i("Loading Failed", "onBudgetLoadingFailure()")
    }

    private fun onBudgetLoadingSuccess(budgets: List<Budget>) {
        budgetAdapter.submitList(budgets)
    }
}