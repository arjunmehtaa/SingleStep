package com.example.singlestep.ui.mytrips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.singlestep.databinding.FragmentMyTripsBinding
import com.example.singlestep.model.TripSummary
import com.example.singlestep.ui.common.adapters.SavedTripAdapter
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.onLoadingFailure

class MyTripsFragment : Fragment() {

    private val viewModel: MyTripsViewModel by viewModels()
    private lateinit var binding: FragmentMyTripsBinding
    private lateinit var tripAdapter: SavedTripAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyTripsBinding.inflate(inflater, container, false)
        setupObservers()
        setupViews()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.tripSummaryList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> binding.shimmerLayout.startShimmer()
                is Result.Failure -> onLoadingFailure()
                is Result.Success -> onTripsLoadingSuccess(result.value)
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            tripAdapter = SavedTripAdapter(
                tripClickListener = {
                    val action =
                        MyTripsFragmentDirections.actionMyTripsFragmentToSummaryFragment(it, true)
                    findNavController().navigate(action)
                },
                removeTripClickListener = {
                    shimmerLayout.visibility = View.VISIBLE
                    shimmerLayout.startShimmer()
                    viewModel.removeFromRoomDatabase(it.toRoomTripSummary(true))
                    viewModel.getTripSummaryList()
                }
            )
            myTripsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            myTripsRecyclerView.adapter = tripAdapter

            startSearchingButton.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private fun onTripsLoadingSuccess(tripSummaries: List<TripSummary>) {
        binding.shimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        if (tripSummaries.isEmpty()) {
            binding.noSavedTripsLayout.visibility = View.VISIBLE
            binding.myTripsRecyclerView.visibility = View.GONE
        } else {
            binding.noSavedTripsLayout.visibility = View.GONE
            binding.myTripsRecyclerView.visibility = View.VISIBLE
        }
        tripAdapter.submitList(tripSummaries)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTripSummaryList()
    }
}