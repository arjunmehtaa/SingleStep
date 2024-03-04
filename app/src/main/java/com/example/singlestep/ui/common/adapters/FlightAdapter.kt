package com.example.singlestep.ui.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amadeus.android.domain.resources.FlightOfferSearch
import com.example.singlestep.databinding.ItemFlightBinding

class FlightAdapter(
    private val fragContext: Context,
    private val clickListener: (FlightOfferSearch) -> Unit
) : ListAdapter<FlightOfferSearch, FlightAdapter.FlightViewHolder>(REPO_COMPARATOR) {

    inner class FlightViewHolder(private val binding: ItemFlightBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var flightSegmentAdapter1: FlightSegmentAdapter
        private lateinit var flightSegmentAdapter2: FlightSegmentAdapter
        fun bind(flight: FlightOfferSearch) {
            with(binding) {
                // Updated line for handling nullable List<String>
                nameTextView.text = flight.validatingAirlineCodes?.joinToString(separator = ", ") ?: "Unknown Airlines"
                priceTextView.text = "${flight.price?.currency} ${flight.price?.grandTotal}"

                flightSegmentAdapter1 = FlightSegmentAdapter(flight, clickListener)
                it1SegmentsRecyclerView.layoutManager = LinearLayoutManager(fragContext)
                it1SegmentsRecyclerView.adapter = flightSegmentAdapter1
                flightSegmentAdapter1.submitList(flight.itineraries?.get(0)?.segments)

                //for possible two-way flight offer
                if(!flight.itineraries.isNullOrEmpty() && flight.itineraries!!.size > 1){
                    flightSegmentAdapter2 = FlightSegmentAdapter(flight, clickListener)
                    it2SegmentsRecyclerView.layoutManager = LinearLayoutManager(fragContext)
                    it2SegmentsRecyclerView.adapter = flightSegmentAdapter2
                    flightSegmentAdapter2.submitList(flight.itineraries?.get(1)?.segments)
                }

                root.setOnClickListener {
                    clickListener(flight)
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
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<FlightOfferSearch>() {
            override fun areItemsTheSame(oldItem: FlightOfferSearch, newItem: FlightOfferSearch): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: FlightOfferSearch, newItem: FlightOfferSearch): Boolean =
                oldItem == newItem
        }
    }
}
