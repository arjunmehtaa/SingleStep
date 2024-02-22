package com.example.singlestep.model

data class HotelOfferResponse(
    val data: List<HotelOffer>
)

data class HotelOffer(
    val type: String,
    val hotel: Hotel,
    val available: Boolean,
    val offers: List<Offer>,
    val self: String
)

data class Hotel(
    val type: String,
    val hotelId: String,
    val chainCode: String,
    val dupeId: String,
    val name: String,
    val cityCode: String,
    val latitude: Double,
    val longitude: Double
)

data class Offer(
    val id: String,
    val checkInDate: String,
    val checkOutDate: String,
    val rateCode: String,
    val rateFamilyEstimated: RateFamilyEstimated,
    val room: Room,
    val guests: Guests,
    val price: Price,
    val policies: Policies,
    val self: String
)

data class RateFamilyEstimated(
    val code: String,
    val type: String
)

data class Room(
    val type: String,
    val typeEstimated: TypeEstimated,
    val description: Description
)

data class TypeEstimated(
    val category: String,
    val beds: Int,
    val bedType: String
)

data class Description(
    val text: String,
    val lang: String
)

data class Guests(
    val adults: Int
)

data class Price(
    val currency: String,
    val base: String,
    val total: String,
    val variations: Variations
)

data class Variations(
    val average: Average,
    val changes: List<Change>
)

data class Average(
    val base: String
)

data class Change(
    val startDate: String,
    val endDate: String,
    val total: String
)

data class Policies(
    val paymentType: String,
    val cancellation: Cancellation
)

data class Cancellation(
    val description: Description,
    val type: String
)

data class HotelResponse(
    val data: List<HotelId>
)

data class HotelId(
    val hotelId: String,
)
