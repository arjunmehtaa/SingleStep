package com.example.singlestep.ui.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amadeus.android.domain.resources.FlightOfferSearch
import com.amadeus.android.domain.resources.FlightOfferSearch.SearchSegment
import com.example.singlestep.databinding.ItemFlightSegmentBinding

class FlightSegmentAdapter(
//    for navigation
    private val flight: FlightOfferSearch,
    private val clickListener: (FlightOfferSearch) -> Unit
):
    ListAdapter<SearchSegment, FlightSegmentAdapter.FlightSegmentViewHolder>(
        REPO_COMPARATOR
    ) {

    inner class FlightSegmentViewHolder(private val binding: ItemFlightSegmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(segment: SearchSegment) {
            with(binding) {
                val departureTimeText = segment.departure?.at ?: "MMMM DD, YYYYTXX:XX"
                departureDateTextView.text = departureTimeText.substring(0, departureTimeText.indexOf("T"))
                departureTimeTextView.text = departureTimeText.substring(departureTimeText.indexOf("T")+1, departureTimeText.length-3)
                val arrivalTimeText = segment.arrival?.at ?: "MMMM DD, YYYYTXX:XX"
                arrivalDateTextView.text = arrivalTimeText.substring(0, departureTimeText.indexOf("T"))
                arrivalTimeTextView.text = arrivalTimeText.substring(departureTimeText.indexOf("T")+1, arrivalTimeText.length-3)

                //todo: IATA code to be converted to city names
                departureCityTextView.text = segment.departure?.iataCode ?: "Departure City"
                arrivalCityTextView.text = segment.arrival?.iataCode ?: "Departure City"

                //todo: IATA code to be converted to airline names
                airlineTextView.text = segment.carrierCode

                flightNumberTextView.text = segment.number

                root.setOnClickListener {
                    clickListener(flight)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FlightSegmentViewHolder {
        val binding =
            ItemFlightSegmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return FlightSegmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlightSegmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<SearchSegment>() {
            override fun areItemsTheSame(oldItem: SearchSegment, newItem: SearchSegment): Boolean =
                oldItem.number== newItem.number

            override fun areContentsTheSame(oldItem: SearchSegment, newItem: SearchSegment): Boolean =
                oldItem == newItem
        }
    }
}