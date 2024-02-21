package com.example.singlestep.ui.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.singlestep.databinding.ItemHotelofferBinding
import com.example.singlestep.model.HotelOffer

class HotelOfferAdapter : ListAdapter<HotelOffer, HotelOfferAdapter.HotelOfferViewHolder>(REPO_COMPARATOR) {

    private lateinit var touristAttractionImageAdapter: TouristAttractionImageAdapter

    inner class HotelOfferViewHolder(private val binding: ItemHotelofferBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hotelOffer: HotelOffer) {
            with(binding) {
                val hotel = hotelOffer.hotel

                // arbitrary but take first offer for now -> can be tweaked
                val hotel_hotelOffer = hotelOffer.offers[0]
                hotel.name.let {
                    nameTextView.text = it
                }
                hotel_hotelOffer.let {
                    priceTextView.text = String.format("%s %s total", it.price.total.toString(), it.price.currency)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelOfferViewHolder {
        val binding =
            ItemHotelofferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotelOfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotelOfferViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<HotelOffer>() {
            override fun areItemsTheSame(oldItem: HotelOffer, newItem: HotelOffer): Boolean =
                oldItem.hotel.hotelId == newItem.hotel.hotelId

            override fun areContentsTheSame(oldItem: HotelOffer, newItem: HotelOffer): Boolean =
                oldItem.hotel.hotelId == newItem.hotel.hotelId
        }
    }
}