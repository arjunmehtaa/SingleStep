package com.example.singlestep.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singlestep.databinding.ItemDestinationBinding
import com.example.singlestep.model.Destination

class DestinationAdapter :
    ListAdapter<Destination, DestinationAdapter.DestinationViewHolder>(REPO_COMPARATOR) {

    inner class DestinationViewHolder(private val binding: ItemDestinationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(destination: Destination) {
            with(binding) {
                destinationNameTextView.text = destination.name
                Glide.with(root.context).load(destination.imageUrl).into(destinationImageView);
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val binding =
            ItemDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DestinationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Destination>() {
            override fun areItemsTheSame(oldItem: Destination, newItem: Destination): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Destination, newItem: Destination): Boolean =
                oldItem == newItem
        }
    }
}