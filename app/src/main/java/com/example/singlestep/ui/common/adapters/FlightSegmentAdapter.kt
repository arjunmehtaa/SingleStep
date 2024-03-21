package com.example.singlestep.ui.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.singlestep.R
import com.example.singlestep.databinding.ItemFlightSegmentBinding
import com.example.singlestep.model.Segment

class FlightSegmentAdapter(
    private val clickListener: () -> Unit
) :
    ListAdapter<Segment, FlightSegmentAdapter.FlightSegmentViewHolder>(
        REPO_COMPARATOR
    ) {

    inner class FlightSegmentViewHolder(private val binding: ItemFlightSegmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(segment: Segment) {
            with(binding) {
                departureAirportDateTextView.text = root.context.getString(
                    R.string.airport_date,
                    segment.startAirport,
                    segment.startDate
                )

                departureTimeTextView.text = segment.startTime

                arrivalAirportDateTextView.text = root.context.getString(
                    R.string.airport_date,
                    segment.endAirport,
                    segment.endDate
                )

                arrivalTimeTextView.text = segment.endTime

                airlineTextView.text = segment.flightCode

                flightDurationTextView.text = segment.duration

                root.setOnClickListener {
                    clickListener()
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
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Segment>() {
            override fun areItemsTheSame(oldItem: Segment, newItem: Segment): Boolean =
                oldItem.number == newItem.number

            override fun areContentsTheSame(
                oldItem: Segment,
                newItem: Segment
            ): Boolean =
                oldItem == newItem
        }
    }
}