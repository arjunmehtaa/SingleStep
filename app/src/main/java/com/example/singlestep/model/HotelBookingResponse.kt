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
    val displayPrice: DisplayPrice
)

data class DisplayPrice(
    val amountPerStay: AmountPerStay
)

data class AmountPerStay(
    val currency: String,
    val amountUnformatted: Double,
    val amountRounded: String
)

data class StarRating(
    val value: Double
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
    val starRating: StarRating,
    val photos: Photo
)