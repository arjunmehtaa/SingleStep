package com.example.singlestep.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amadeus.android.domain.resources.FlightOfferSearch
import com.bumptech.glide.Glide
import com.example.singlestep.R
import com.example.singlestep.databinding.FragmentSummaryBinding
import com.example.singlestep.model.Hotel
import com.example.singlestep.model.TripParameters
import com.example.singlestep.ui.common.adapters.FlightAdapter
import com.example.singlestep.ui.common.adapters.HotelAdapter
import com.example.singlestep.utils.getSampleAIResponse
import com.example.singlestep.utils.hideBottomNavigationBar
import com.example.singlestep.utils.showBottomNavigationBar
import com.example.singlestep.viewmodel.SummaryViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse

class SummaryFragment : Fragment() {

    private val viewModel: SummaryViewModel by viewModels()
    private lateinit var binding: FragmentSummaryBinding
    private lateinit var flightAdapter: FlightAdapter
    private lateinit var hotelAdapter: HotelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSummaryBinding.inflate(inflater, container, false)
        setupObservers()
        arguments?.let { bundle ->
            val args = SummaryFragmentArgs.fromBundle(bundle)
            val tripParameters = args.tripParameters
            val hotel = args.hotel
            val flight = args.flight
            val airlineName = args.airlineName
            val airlineICAOCode = args.airlineICAOCode
            setupViews(tripParameters, hotel, flight, airlineName, airlineICAOCode)
        }
        return binding.root
    }

    private fun setupObservers() {
    }

    private fun setupViews(
        tripParameters: TripParameters,
        hotel: Hotel,
        flight: FlightOfferSearch,
        airlineName: String,
        airlineICAOCode: String
    ) {
        with(binding) {

            titleTextView.text = getString(R.string.trip_to, tripParameters.destination.city)

            setupCityImage(tripParameters)

            flightAdapter = FlightAdapter(
                tripParameters.source.city,
                tripParameters.destination.city,
                tripParameters.guests,
                airlineNameGetter = {
                    airlineName
                },
                airlineICAOCodeGetter = {
                    airlineICAOCode
                },
                clickListener = { _, _, _ ->
                }
            )
            flightRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            flightRecyclerView.adapter = flightAdapter
            flightAdapter.submitList(listOf(flight))

            hotelAdapter = HotelAdapter(
                tripParameters.checkInDate,
                tripParameters.checkOutDate,
                tripParameters.guests
            ) {
            }
            hotelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            hotelRecyclerView.adapter = hotelAdapter
            hotelAdapter.submitList(listOf(hotel))

            tripSummaryTextView.text = getSampleAIResponse()
        }
    }

    private fun setupCityImage(tripParameters: TripParameters) {
        val photoUrl = tripParameters.destination.imageUrl
        if (photoUrl != null) {
            Glide.with(binding.root.context).load(photoUrl).into(binding.cityImageView)
        } else {
            val photoMetadata = tripParameters.destination.photoMetadata
            if (photoMetadata != null) {
                val placesClient = Places.createClient(binding.root.context)
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(1000)
                    .setMaxHeight(300)
                    .build()
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                        val bitmap = fetchPhotoResponse.bitmap
                        binding.cityImageView.setImageBitmap(bitmap)
                    }
            }
        }
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