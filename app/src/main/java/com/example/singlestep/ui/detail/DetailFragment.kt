package com.example.singlestep.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.singlestep.databinding.FragmentDetailBinding
import com.example.singlestep.model.HotelOffer
import com.example.singlestep.model.TripParameters
import com.example.singlestep.ui.common.adapters.HotelOfferAdapter
import com.example.singlestep.utils.Result


class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding
    private lateinit var hotelOfferAdapter: HotelOfferAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        setupObservers()
        arguments?.let { bundle ->
            var tripParameters = DetailFragmentArgs.fromBundle(bundle).tripParameters
            setupViews(tripParameters)
            viewModel.getHotelOffersList(tripParameters)
        }

        return binding.root
    }

    private fun setupObservers() {
        viewModel.hotelOffersList.observe(viewLifecycleOwner) {result ->
            when(result) {
                Result.Loading -> binding.shimmerLayout.startShimmer()
                is Result.Failure -> {
                    Log.i("DEBUG: ", result.throwable.stackTraceToString())
                }
                is Result.Success -> onHotelOffersLoadingSuccess(result.value)
            }

        }
    }
    private fun setupViews(tripParameters: TripParameters) {
        with(binding) {
            backButton.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            hotelOfferAdapter = HotelOfferAdapter()
            hotelOffersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            hotelOffersRecyclerView.adapter = hotelOfferAdapter
        }
    }

    private fun onHotelOffersLoadingSuccess(hotelOffers: List<HotelOffer>) {
        binding.shimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        hotelOfferAdapter.submitList(hotelOffers)
    }
}