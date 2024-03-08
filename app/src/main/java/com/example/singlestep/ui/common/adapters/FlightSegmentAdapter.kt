package com.example.singlestep.ui.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amadeus.android.domain.resources.FlightOfferSearch
import com.amadeus.android.domain.resources.FlightOfferSearch.SearchSegment
import com.example.singlestep.R
import com.example.singlestep.databinding.ItemFlightSegmentBinding
import com.example.singlestep.utils.formatDuration
import com.example.singlestep.utils.getDateAndMonthName
import com.example.singlestep.utils.getFormattedTime

class FlightSegmentAdapter(
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
                departureAirportDateTextView.text = root.context.getString(
                    R.string.airport_date,
                    segment.departure?.iataCode,
                    getDateAndMonthName(
                        departureTimeText.substring(
                            0,
                            departureTimeText.indexOf("T")
                        )
                    )
                )

                departureTimeTextView.text = getFormattedTime(
                    departureTimeText.substring(
                        departureTimeText.indexOf("T") + 1,
                        departureTimeText.length - 3
                    )
                )

                val arrivalTimeText = segment.arrival?.at ?: "MMMM DD, YYYYTXX:XX"
                arrivalAirportDateTextView.text = root.context.getString(
                    R.string.airport_date,
                    segment.arrival?.iataCode,
                    getDateAndMonthName(arrivalTimeText.substring(0, arrivalTimeText.indexOf("T")))
                )

                arrivalTimeTextView.text = getFormattedTime(
                    arrivalTimeText.substring(
                        departureTimeText.indexOf("T") + 1,
                        arrivalTimeText.length - 3
                    )
                )

                airlineTextView.text =
                    root.context.getString(
                        R.string.airline_name,
                        segment.carrierCode,
                        segment.number
                    )

                flightDurationTextView.text = formatDuration(segment.duration)

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