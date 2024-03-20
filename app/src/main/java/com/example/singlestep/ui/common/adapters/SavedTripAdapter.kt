package com.example.singlestep.ui.common.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singlestep.R
import com.example.singlestep.databinding.ItemSavedTripBinding
import com.example.singlestep.model.TripSummary
import com.example.singlestep.utils.formatBudget
import com.example.singlestep.utils.getFormattedDate
import com.example.singlestep.utils.getRemoveTripOnClickListener
import loadBitmapFromFile

class SavedTripAdapter(
    private val tripClickListener: (TripSummary) -> Unit,
    private val removeTripClickListener: (TripSummary) -> Unit
) : ListAdapter<TripSummary, SavedTripAdapter.SavedTripViewHolder>(REPO_COMPARATOR) {

    inner class SavedTripViewHolder(private val binding: ItemSavedTripBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tripSummary: TripSummary) {
            with(binding) {
                fromToTextView.text = root.context.getString(
                    R.string.from_to,
                    tripSummary.tripParameters.source.city,
                    tripSummary.tripParameters.destination.city
                )
                datesTextView.text = root.context.getString(
                    R.string.date_range,
                    getFormattedDate(tripSummary.tripParameters.checkInDate),
                    getFormattedDate(tripSummary.tripParameters.checkOutDate),
                )
                budgetTextView.text = root.context.getString(
                    R.string.budget_text,
                    formatBudget(tripSummary.tripParameters.budget.toInt().toString()).second
                )
                guestsTextView.text = root.context.getString(
                    R.string.number_of_guests,
                    tripSummary.tripParameters.guests
                )
                viewTripButton.setOnClickListener {
                    tripClickListener(tripSummary)
                }
                removeTripButton.setOnClickListener(
                    getRemoveTripOnClickListener(root.context) {
                        removeTripClickListener(tripSummary)
                    }
                )
                val photoUrl = tripSummary.tripParameters.destination.imageUrl
                if (photoUrl != null) {
                    Glide.with(binding.root.context).load(photoUrl).into(binding.cityImageView)
                } else {
                    val imageFileUri = tripSummary.tripParameters.destination.imageFileUri
                    binding.cityImageView.setImageBitmap(imageFileUri?.let {
                        loadBitmapFromFile(
                            root.context, it
                        )
                    })
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SavedTripViewHolder {
        val binding = ItemSavedTripBinding.inflate(
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