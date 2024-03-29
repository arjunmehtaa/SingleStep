package com.example.singlestep.ui.summary

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
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
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.getRemoveTripOnClickListener
import com.example.singlestep.utils.hideBottomNavigationBar
import com.example.singlestep.utils.loadBitmapFromFile
import com.example.singlestep.utils.saveBitmapToFile
import com.example.singlestep.utils.showBottomNavigationBar
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
        arguments?.let { bundle ->
            val args = SummaryFragmentArgs.fromBundle(bundle)
            val tripSummary = args.tripSummary
            val fromMyTrips = args.fromMyTrips
            setupObservers(tripSummary)
            setupViews(tripSummary, fromMyTrips)
        }
        return binding.root
    }

    private fun setupObservers(tripSummary: TripSummary) {
        viewModel.itineraryString.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.Loading -> onItineraryLoading()
                is Result.Failure -> onItineraryLoadingFailure(result, tripSummary)
                is Result.Success -> onItineraryLoadingSuccess(result.value, tripSummary)
            }
        }
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

            if (!fromMyTrips) {
                viewModel.getItinerary()
                saveButton.setOnClickListener {
                    viewModel.saveToRoomDatabase(tripSummary.toRoomTripSummary())
                    Toast.makeText(context, "Successfully added to My Trips", Toast.LENGTH_SHORT)
                        .show()
                    val action = SummaryFragmentDirections.actionSummaryFragmentToSearchFragment()
                    findNavController().navigate(action)
                }
            } else {
                onItineraryLoadingSuccess(tripSummary.itinerarySummary, tripSummary)
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

    private fun onItineraryLoading() {
        Log.d("SummaryFragment", "Loading itinerary")
        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.failedSummaryLayout.visibility = View.GONE
        binding.summaryLayout.visibility = View.GONE
    }

    private fun onItineraryLoadingSuccess(itinerary: String, tripSummary: TripSummary) {
        Log.d("SummaryFragment", "Itinerary loaded successfully")
        tripSummary.itinerarySummary = itinerary
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.tripSummaryTextView.text =
            Html.fromHtml(itinerary, HtmlCompat.FROM_HTML_MODE_LEGACY)
        if (itinerary.isBlank()) {
            binding.failedSummaryLayout.visibility = View.VISIBLE
            binding.summaryErrorTextView.text = getString(R.string.summary_empty_response)
        } else {
            binding.failedSummaryLayout.visibility = View.GONE
            binding.summaryLayout.visibility = View.VISIBLE
        }
    }

    private fun onItineraryLoadingFailure(result: Result.Failure, tripSummary: TripSummary) {
        Log.e("SummaryFragment", "Error loading itinerary", result.throwable)
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.summaryLayout.visibility = View.GONE
        binding.failedSummaryLayout.visibility = View.VISIBLE
        binding.summaryErrorTextView.text = getString(R.string.summary_failed_response)
        checkIfConnectionRestored(tripSummary)
    }

    private fun checkIfConnectionRestored(tripSummary: TripSummary) {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (tripSummary.itinerarySummary.isBlank()) {
                    viewModel.getItinerary()
                }
            }
        })
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