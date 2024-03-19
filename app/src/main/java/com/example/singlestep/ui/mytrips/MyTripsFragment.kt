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
import com.example.singlestep.ui.common.adapters.SavedTripAdapter
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.onLoading
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
                Result.Loading -> onLoading()
                is Result.Failure -> onLoadingFailure()
                is Result.Success -> tripAdapter.submitList(result.value)
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            tripAdapter = SavedTripAdapter() {
                val action =
                    MyTripsFragmentDirections.actionMyTripsFragmentToSummaryFragment(it, true)
                findNavController().navigate(action)
            }
            myTripsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            myTripsRecyclerView.adapter = tripAdapter
            titleTextView.setOnClickListener {
                viewModel.deleteAll()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTripSummaryList()
    }
}