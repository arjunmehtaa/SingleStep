package com.example.singlestep.ui.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.singlestep.databinding.ItemTripInfoBinding
import com.example.singlestep.model.TripSummary

class SavedTripAdapter(
    private val clickListener: (TripSummary) -> Unit
) : ListAdapter<TripSummary, SavedTripAdapter.SavedTripViewHolder>(REPO_COMPARATOR) {

    inner class SavedTripViewHolder(private val binding: ItemTripInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tripSummary: TripSummary) {
            with(binding) {
//              todo: convert content read from file to text
                locationsTextView.text =
                    "${tripSummary.tripParameters.source.city} - ${tripSummary.tripParameters.destination.city}{}"
                datesTextView.text = "2023-03-04 - 2023-03-31"
                guestTextView.text = tripSummary.tripParameters.guests.toString()
                savedTimeTextView.text = "last saved 2023-03-04 3:21"
                root.setOnClickListener {
                    clickListener(tripSummary)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SavedTripViewHolder {
        val binding = ItemTripInfoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SavedTripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedTripViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<TripSummary>() {
            override fun areItemsTheSame(oldItem: TripSummary, newItem: TripSummary): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: TripSummary, newItem: TripSummary): Boolean =
                oldItem == newItem
        }
    }
}