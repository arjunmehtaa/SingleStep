package com.example.singlestep.model

data class HotelBookingResponse(
    val data: Hotels
)

data class Hotels(
    val results: List<Hotel>
)

data class Hotel(
    val displayName: DisplayName,
    val priceDisplayInfo: PriceDisplayInfo,
    val basicPropertyData: BasicPropertyData
)

data class DisplayName(
    val text: String
)

data class PriceDisplayInfo(
    val displayPrice: DisplayPrice,
    val priceBeforeDiscount: PriceBeforeDiscount
)

data class DisplayPrice(
    val amountPerStay: AmountPerStay
)

data class AmountPerStay(
    val currency: String,
    val amountUnformatted: Double,
    val amountRounded: String
)

data class Photo(
    val main: MainPhoto
)

data class MainPhoto(
    val lowResJpegUrl: AbsoluteUrl
)

data class AbsoluteUrl(
    val absoluteUrl: String
)

data class BasicPropertyData(
    val photos: Photo,
    val location: HotelLocation,
    val reviews: Reviews
)

data class HotelLocation(
    val address: String
)

data class PriceBeforeDiscount(
    val amountPerStay: AmountPerStay,
)

data class Reviews(
    val totalScoreTextTag: TotalScoreTextTag,
    val totalScore: Double
)

data class TotalScoreTextTag(
    val translation: String
)