package com.example.singlestep.ui.hotels

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.singlestep.databinding.FragmentHotelBinding
import com.example.singlestep.model.Hotel
import com.example.singlestep.model.TripParameters
import com.example.singlestep.ui.common.adapters.HotelAdapter
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.hideBottomNavigationBar
import com.example.singlestep.utils.showBottomNavigationBar


class HotelFragment : Fragment() {

    private val viewModel: HotelViewModel by viewModels()
    private lateinit var binding: FragmentHotelBinding
    private lateinit var hotelAdapter: HotelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHotelBinding.inflate(inflater, container, false)
        setupObservers()
        arguments?.let { bundle ->
            val tripParameters = HotelFragmentArgs.fromBundle(bundle).tripParameters
            viewModel.setTripParameters(tripParameters)
            Log.d("HotelFragment", "Received tripParameters: $tripParameters")
            setupViews(tripParameters)
        }

        return binding.root
    }

    private fun setupObservers() {
        viewModel.hotelList.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> {
                    Log.d("HotelFragment", "Loading hotels")
                    binding.shimmerLayout.startShimmer()
                }

                is Result.Failure -> {
                    Log.e("HotelFragment", "Error loading hotels", result.throwable)
                }

                is Result.Success -> {
                    Log.d("HotelFragment", "Hotels loaded successfully")
                    onHotelOffersLoadingSuccess(result.value)
                }
            }
        }
    }

    private fun setupViews(tripParameters: TripParameters) {
        with(binding) {
            hotelAdapter = HotelAdapter(
                tripParameters.checkInDate,
                tripParameters.checkOutDate,
                tripParameters.guests
            ) { hotel ->
            }
            hotelsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            hotelsRecyclerView.adapter = hotelAdapter
            backButton.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private fun onHotelOffersLoadingSuccess(hotels: List<Hotel>) {
        binding.shimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        hotelAdapter.submitList(hotels)
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigationBar(activity)
    }

    override fun onStop() {
        super.onStop()
        showBottomNavigationBar(activity)
    }
}
