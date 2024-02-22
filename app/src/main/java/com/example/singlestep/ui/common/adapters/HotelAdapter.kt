package com.example.singlestep.ui.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singlestep.databinding.ItemHotelBinding
import com.example.singlestep.model.Hotel

class HotelAdapter : ListAdapter<Hotel, HotelAdapter.HotelOfferViewHolder>(REPO_COMPARATOR) {

    inner class HotelOfferViewHolder(private val binding: ItemHotelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hotel: Hotel) {
            with(binding) {
                nameTextView.text = hotel.displayName.text
                priceTextView.text = hotel.priceDisplayInfo.displayPrice.amountPerStay.amountRounded
                Glide.with(root.context)
                    .load(hotel.basicPropertyData.photos.main.lowResJpegUrl.absoluteUrl)
                    .into(hotelImageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelOfferViewHolder {
        val binding =
            ItemHotelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotelOfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotelOfferViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Hotel>() {
            override fun areItemsTheSame(oldItem: Hotel, newItem: Hotel): Boolean =
                oldItem.displayName.text == newItem.displayName.text

            override fun areContentsTheSame(oldItem: Hotel, newItem: Hotel): Boolean =
                oldItem == newItem
        }
    }
}