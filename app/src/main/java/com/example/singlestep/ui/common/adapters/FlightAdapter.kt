package com.example.singlestep.ui.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amadeus.android.domain.resources.FlightOfferSearch
import com.example.singlestep.databinding.ItemFlightInfoBinding

class FlightAdapter(
    val clickListener: (FlightOfferSearch) -> Unit
) :
    ListAdapter<FlightOfferSearch, FlightAdapter.FlightViewHolder>(REPO_COMPARATOR) {
    inner class FlightViewHolder(private val binding: ItemFlightInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(flight: FlightOfferSearch) {
            with(binding) {
                nameTextView.text = flight.validatingAirlineCodes.toString()
                priceTextView.text = "${flight.price!!.currency}  ${flight.price!!.grandTotal}"

                /*val itinerary = flight.itineraries
                var legs = "OUTBOUND: \n"
                val legCount = itinerary.departureLocations.size

                for (i in 0..legCount - 1) {
                    if (i == legCount / 2) {
                        legs += "RETURN: \n"
                    }
                    if (itinerary.departureLocations.size > 2 && i % 2 == 1) {
                        legs += "TRANSFER: "
                    }

                    legs += itinerary.departureLocations[i] + "-" + itinerary.arrivalLocations[i] + "\n" +
                            "Departure: " + itinerary.departureTimes[i] + "\n" + "Arrival: " + itinerary.arrivalTimes[i] + "\n\n"
                }
                tripLegs.text = legs*/
                root.setOnClickListener {
                    clickListener(flight)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val binding =
            ItemFlightInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlightViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<FlightOfferSearch>() {
            override fun areItemsTheSame(oldItem: FlightOfferSearch, newItem: FlightOfferSearch): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: FlightOfferSearch, newItem: FlightOfferSearch): Boolean =
                oldItem == newItem
        }
    }
}