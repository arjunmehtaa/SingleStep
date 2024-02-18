package com.example.singlestep.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amadeus.android.domain.resources.Activity
import com.example.singlestep.databinding.FragmentExploreBinding
import com.example.singlestep.ui.common.adapters.TouristAttractionAdapter
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.onLoadingFailure

class ExploreFragment : Fragment() {

    private val viewModel: ExploreViewModel by viewModels()
    private lateinit var binding: FragmentExploreBinding
    private lateinit var touristAttractionAdapter: TouristAttractionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        setupObservers()
        setupViews()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.touristAttractionList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> binding.shimmerLayout.startShimmer()
                is Result.Failure -> onLoadingFailure()
                is Result.Success -> onAttractionsLoadingSuccess(result.value)
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            touristAttractionAdapter = TouristAttractionAdapter {
                touristAttractionsRecyclerView.scrollToPosition(it)
            }
            touristAttractionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            touristAttractionsRecyclerView.adapter = touristAttractionAdapter
        }
    }

    private fun onAttractionsLoadingSuccess(attractions: List<Activity>) {
        binding.shimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        touristAttractionAdapter.submitList(attractions)
    }
}