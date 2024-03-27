package com.example.singlestep.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HotelBookingResponse(
    val data: Hotels
) : Parcelable

@Parcelize
data class Hotels(
    val results: List<Hotel>
) : Parcelable

@Parcelize
data class Hotel(
    val displayName: DisplayName,
    val priceDisplayInfo: PriceDisplayInfo,
    val basicPropertyData: BasicPropertyData
) : Parcelable

@Parcelize
data class DisplayName(
    val text: String
) : Parcelable

@Parcelize
data class PriceDisplayInfo(
    val displayPrice: DisplayPrice,
    val priceBeforeDiscount: PriceBeforeDiscount
) : Parcelable

@Parcelize
data class DisplayPrice(
    val amountPerStay: AmountPerStay
) : Parcelable

@Parcelize
data class AmountPerStay(
    val currency: String,
    val amountUnformatted: Double,
    val amountRounded: String
) : Parcelable

@Parcelize
data class Photo(
    val main: MainPhoto
) : Parcelable

@Parcelize
data class MainPhoto(
    val lowResJpegUrl: AbsoluteUrl
) : Parcelable

@Parcelize
data class AbsoluteUrl(
    val absoluteUrl: String
) : Parcelable

@Parcelize
data class BasicPropertyData(
    val photos: Photo,
    val location: HotelLocation,
    val reviews: Reviews
) : Parcelable

@Parcelize
data class HotelLocation(
    val address: String
) : Parcelable

@Parcelize
data class PriceBeforeDiscount(
    val amountPerStay: AmountPerStay,
) : Parcelable

@Parcelize
data class Reviews(
    val totalScoreTextTag: TotalScoreTextTag?,
    val totalScore: Double
) : Parcelable

@Parcelize
data class TotalScoreTextTag(
    val translation: String?
) : Parcelable