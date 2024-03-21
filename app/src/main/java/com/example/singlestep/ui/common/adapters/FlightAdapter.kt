package com.example.singlestep.ui.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singlestep.R
import com.example.singlestep.databinding.ItemFlightBinding
import com.example.singlestep.model.Flight
import com.example.singlestep.utils.convertToTitleCase

class FlightAdapter(
    private val from: String,
    private val to: String,
    private val guests: Int,
    private val airlineNameGetter: (String?) -> String?,
    private val airlineICAOCodeGetter: (String?) -> String?,
    private val clickListener: (Flight, String?, String?) -> Unit
) : ListAdapter<Flight, FlightAdapter.FlightViewHolder>(REPO_COMPARATOR) {

    inner class FlightViewHolder(private val binding: ItemFlightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var flightSegmentAdapter1: FlightSegmentAdapter
        private lateinit var flightSegmentAdapter2: FlightSegmentAdapter
        fun bind(flight: Flight) {
            with(binding) {
                // Updated line for handling nullable List<String>
                val airlineName = airlineNameGetter(flight.airlineCode)
                if (!airlineName.isNullOrEmpty()) {
                    nameTextView.text = convertToTitleCase(airlineName)
                } else {
                    nameTextView.text = flight.airlineCode
                }

                priceTextView.text = flight.totalPrice

                fromToTextView.text = root.context.getString(R.string.from_to, from, to)
                toFromTextView.text = root.context.getString(R.string.from_to, to, from)
                summaryTextView.text = root.context.getString(R.string.round_trip_guests, guests)

                val airlineICAOCode = airlineICAOCodeGetter(
                    flight.airlineCode
                )
                Glide.with(root.context)
                    .load(
                        "https://raw.githubusercontent.com/sexym0nk3y/airline-logos/main/logos/${airlineICAOCode}.png"
                    )
                    .into(airlineImageView)

                flightSegmentAdapter1 = FlightSegmentAdapter() {
                    clickListener(flight, airlineName, airlineICAOCode)
                }
                it1SegmentsRecyclerView.layoutManager = LinearLayoutManager(root.context)
                it1SegmentsRecyclerView.adapter = flightSegmentAdapter1
                flightSegmentAdapter1.submitList(flight.itineraries?.get(0)?.segments)

                //for possible two-way flight offer
                if (!flight.itineraries.isNullOrEmpty() && flight.itineraries!!.size > 1) {
                    flightSegmentAdapter2 = FlightSegmentAdapter() {
                        clickListener(flight, airlineName, airlineICAOCode)
                    }
                    it2SegmentsRecyclerView.layoutManager = LinearLayoutManager(root.context)
                    it2SegmentsRecyclerView.adapter = flightSegmentAdapter2
                    flightSegmentAdapter2.submitList(flight.itineraries?.get(1)?.segments)
                }

                root.setOnClickListener {
                    clickListener(flight, airlineName, airlineICAOCode)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val binding = ItemFlightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlightViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Flight>() {
            override fun areItemsTheSame(
                oldItem: Flight,
                newItem: Flight
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Flight,
                newItem: Flight
            ): Boolean =
                oldItem == newItem
        }
    }
}
