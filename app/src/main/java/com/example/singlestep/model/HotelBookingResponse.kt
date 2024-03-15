package com.example.singlestep.model

import java.io.Serializable

data class HotelBookingResponse(
    val data: Hotels
) : Serializable

data class Hotels(
    val results: List<Hotel>
) : Serializable

data class Hotel(
    val displayName: DisplayName,
    val priceDisplayInfo: PriceDisplayInfo,
    val basicPropertyData: BasicPropertyData
) : Serializable

data class DisplayName(
    val text: String
) : Serializable

data class PriceDisplayInfo(
    val displayPrice: DisplayPrice,
    val priceBeforeDiscount: PriceBeforeDiscount
) : Serializable

data class DisplayPrice(
    val amountPerStay: AmountPerStay
) : Serializable

data class AmountPerStay(
    val currency: String,
    val amountUnformatted: Double,
    val amountRounded: String
) : Serializable

data class Photo(
    val main: MainPhoto
) : Serializable

data class MainPhoto(
    val lowResJpegUrl: AbsoluteUrl
) : Serializable

data class AbsoluteUrl(
    val absoluteUrl: String
) : Serializable

data class BasicPropertyData(
    val photos: Photo,
    val location: HotelLocation,
    val reviews: Reviews
) : Serializable

data class HotelLocation(
    val address: String
) : Serializable

data class PriceBeforeDiscount(
    val amountPerStay: AmountPerStay,
) : Serializable

data class Reviews(
    val totalScoreTextTag: TotalScoreTextTag,
    val totalScore: Double
) : Serializable

data class TotalScoreTextTag(
    val translation: String
) : Serializable