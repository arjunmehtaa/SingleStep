package com.example.singlestep.ui.common.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.singlestep.databinding.ItemTripInfoBinding

class SavedTripAdapter:
    ListAdapter<String, SavedTripAdapter.SavedTripViewHolder>(
        REPO_COMPARATOR
    ) {

    inner class SavedTripViewHolder(private val binding: ItemTripInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(segment: String) {
            with(binding) {
//              todo: convert content read from file to text
                locationsTextView.text = "New York - Orlando"
                datesTextView.text = "2023-03-04 - 2023-03-31"
                guestTextView.text = "1 Guest"
                savedTimeTextView.text = "last saved 2023-03-04 3:21"
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedTripViewHolder {
        val binding =
            ItemTripInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return SavedTripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedTripViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}