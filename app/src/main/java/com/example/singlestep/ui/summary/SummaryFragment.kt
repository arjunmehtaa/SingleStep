package com.example.singlestep.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.singlestep.R
import com.example.singlestep.databinding.FragmentSummaryBinding
import com.example.singlestep.model.TripParameters
import com.example.singlestep.model.TripSummary
import com.example.singlestep.ui.common.adapters.FlightAdapter
import com.example.singlestep.ui.common.adapters.HotelAdapter
import com.example.singlestep.utils.getRemoveTripOnClickListener
import com.example.singlestep.utils.getSampleAIResponse
import com.example.singlestep.utils.hideBottomNavigationBar
import com.example.singlestep.utils.showBottomNavigationBar
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import loadBitmapFromFile
import saveBitmapToFile

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
            val tripSummary = args.tripSummary
            val fromMyTrips = args.fromMyTrips
            setupViews(tripSummary, fromMyTrips)
        }
        return binding.root
    }

    private fun setupObservers() {
    }

    private fun setupViews(
        tripSummary: TripSummary,
        fromMyTrips: Boolean
    ) {
        with(binding) {

            titleTextView.text =
                getString(R.string.trip_to, tripSummary.tripParameters.destination.city)

            setupCityImage(tripSummary.tripParameters)

            flightAdapter = FlightAdapter(
                tripSummary.tripParameters.source.city,
                tripSummary.tripParameters.destination.city,
                tripSummary.tripParameters.guests,
                airlineNameGetter = {
                    tripSummary.airlineName
                },
                airlineICAOCodeGetter = {
                    tripSummary.airlineICAOCode
                },
                clickListener = { _, _, _ ->
                }
            )
            flightRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            flightRecyclerView.adapter = flightAdapter
            flightAdapter.submitList(listOf(tripSummary.flight))

            hotelAdapter = HotelAdapter(
                tripSummary.tripParameters.checkInDate,
                tripSummary.tripParameters.checkOutDate,
                tripSummary.tripParameters.guests
            ) {
            }
            hotelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            hotelRecyclerView.adapter = hotelAdapter
            hotelAdapter.submitList(listOf(tripSummary.hotel))

            tripSummaryTextView.text = getSampleAIResponse()

            if (!fromMyTrips) {
                saveButton.setOnClickListener {
                    viewModel.saveToRoomDatabase(tripSummary.toRoomTripSummary())
                    Toast.makeText(context, "Successfully added to My Trips", Toast.LENGTH_SHORT)
                        .show()
                    val action = SummaryFragmentDirections.actionSummaryFragmentToSearchFragment()
                    findNavController().navigate(action)
                }
            } else {
                removeButton.visibility = View.VISIBLE
                saveButton.visibility = View.INVISIBLE
                saveButton.isClickable = false
                removeButton.setOnClickListener(
                    getRemoveTripOnClickListener(root.context) {
                        viewModel.removeFromRoomDatabase(tripSummary.toRoomTripSummary(true))
                        val action =
                            SummaryFragmentDirections.actionSummaryFragmentToMyTripsFragment()
                        findNavController().navigate(action)
                    }
                )
            }

            backButton.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

        }
    }

    private fun setupCityImage(tripParameters: TripParameters) {
        val photoUrl = tripParameters.destination.imageUrl
        if (photoUrl != null) {
            Glide.with(binding.root.context).load(photoUrl).into(binding.cityImageView)
        } else {
            val photoMetadata = tripParameters.destination.photoMetadata
            val imageFileUri = tripParameters.destination.imageFileUri
            if (imageFileUri != null) {
                binding.cityImageView.setImageBitmap(
                    loadBitmapFromFile(
                        requireContext(),
                        imageFileUri
                    )
                )
            } else if (photoMetadata != null) {
                val placesClient = Places.createClient(binding.root.context)
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500)
                    .setMaxHeight(250)
                    .build()
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                        val bitmap = fetchPhotoResponse.bitmap
                        binding.cityImageView.setImageBitmap(bitmap)
                        val file = saveBitmapToFile(requireContext(), bitmap)
                        tripParameters.destination.imageFileUri = file?.absolutePath
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