package com.example.singlestep.ui.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singlestep.R
import com.example.singlestep.databinding.ItemTouristAttractionImageBinding

class TouristAttractionImageAdapter :
    ListAdapter<String, TouristAttractionImageAdapter.TouristAttractionImageViewHolder>(
        REPO_COMPARATOR
    ) {

    inner class TouristAttractionImageViewHolder(private val binding: ItemTouristAttractionImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            with(binding) {
                Glide.with(root.context)
                    .load(imageUrl)
                    .error(R.drawable.image_loading_error)
                    .into(destinationImageView)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TouristAttractionImageViewHolder {
        val binding =
            ItemTouristAttractionImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return TouristAttractionImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TouristAttractionImageViewHolder, position: Int) {
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