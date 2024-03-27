package com.example.singlestep.ui.common.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singlestep.R
import com.example.singlestep.databinding.ItemHotelBinding
import com.example.singlestep.model.Hotel
import com.example.singlestep.utils.getDaysBetween

class HotelAdapter(
    val checkInDate: String,
    val checkOutDate: String,
    val guests: Int,
    private val onClick: (Hotel) -> Unit
) : ListAdapter<Hotel, HotelAdapter.HotelOfferViewHolder>(REPO_COMPARATOR) {

    inner class HotelOfferViewHolder(private val binding: ItemHotelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hotel: Hotel) {
            with(binding) {

                nameTextView.text = hotel.displayName.text

                addressTextView.text = hotel.basicPropertyData.location.address

                val reviews = hotel.basicPropertyData.reviews
                if (reviews.totalScore != 0.0) {
                    ratingTextView.text = reviews.totalScore.toString()
                    ratingKeywordTextView.text = reviews.totalScoreTextTag?.translation
                    ratingTextView.visibility = View.VISIBLE
                    ratingKeywordTextView.visibility = View.VISIBLE
                } else {
                    ratingTextView.visibility = View.GONE
                    ratingKeywordTextView.visibility = View.GONE
                }

                val days = getDaysBetween(checkInDate, checkOutDate)
                summaryTextView.text =
                    root.context.getString(R.string.nights_days_guests, days - 1, days, guests)

                val priceBeforeDiscount =
                    hotel.priceDisplayInfo.priceBeforeDiscount.amountPerStay
                val currentPrice = hotel.priceDisplayInfo.displayPrice.amountPerStay
                if (priceBeforeDiscount.amountRounded.isNotBlank() && priceBeforeDiscount.amountUnformatted > currentPrice.amountUnformatted) {
                    priceBeforeDiscountTextView.text = priceBeforeDiscount.amountRounded
                    priceBeforeDiscountTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    priceBeforeDiscountTextView.visibility = View.VISIBLE
                } else {
                    priceBeforeDiscountTextView.visibility = View.GONE
                }

                priceTextView.text = currentPrice.amountRounded

                Glide.with(root.context)
                    .load(hotel.basicPropertyData.photos.main.lowResJpegUrl.absoluteUrl)
                    .into(hotelImageView)

                root.setOnClickListener {
                    onClick(hotel)
                }
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