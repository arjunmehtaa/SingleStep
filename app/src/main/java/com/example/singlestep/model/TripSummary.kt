package com.example.singlestep.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TripSummary(
    val id: Int? = 0,
    val tripParameters: TripParameters,
    val hotel: Hotel,
    val flight: Flight,
    val airlineName: String,
    val airlineICAOCode: String,
    var itinerarySummary: String,
) : Parcelable {
    fun toRoomTripSummary(shouldIncludeId: Boolean = false): RoomTripSummary {
        val roomTripSummary = RoomTripSummary(
            sourceCity = tripParameters.source.city,
            sourceImageUrl = tripParameters.source.imageUrl,
            sourceLatitude = tripParameters.source.latitude,
            sourceLongitude = tripParameters.source.longitude,
            destinationCity = tripParameters.destination.city,
            destinationImageUrl = tripParameters.destination.imageUrl,
            destinationLatitude = tripParameters.destination.latitude,
            destinationLongitude = tripParameters.destination.longitude,
            cityImageFileUri = tripParameters.destination.imageFileUri,
            checkInDate = tripParameters.checkInDate,
            checkOutDate = tripParameters.checkOutDate,
            originalBudget = tripParameters.originalBudget,
            remainingBudget = tripParameters.remainingBudget,
            guests = tripParameters.guests,
            hotelName = hotel.displayName.text,
            hotelCurrency = hotel.priceDisplayInfo.displayPrice.amountPerStay.currency,
            hotelAmountUnformatted = hotel.priceDisplayInfo.displayPrice.amountPerStay.amountUnformatted,
            hotelAmountRounded = hotel.priceDisplayInfo.displayPrice.amountPerStay.amountRounded,
            hotelCurrencyBeforeDiscount = hotel.priceDisplayInfo.priceBeforeDiscount.amountPerStay.currency,
            hotelAmountUnformattedBeforeDiscount = hotel.priceDisplayInfo.priceBeforeDiscount.amountPerStay.amountUnformatted,
            hotelAmountRoundedBeforeDiscount = hotel.priceDisplayInfo.priceBeforeDiscount.amountPerStay.amountRounded,
            hotelPhotoUrl = hotel.basicPropertyData.photos.main.lowResJpegUrl.absoluteUrl,
            hotelAddress = hotel.basicPropertyData.location.address,
            hotelScore = hotel.basicPropertyData.reviews.totalScore,
            hotelScoreTag = hotel.basicPropertyData.reviews.totalScoreTextTag?.translation,
            flightId = flight.id,
            flightAirlineCode = flight.airlineCode,
            flightRawPrice = flight.rawPrice,
            flightTotalPrice = flight.totalPrice,
            flightItinerary1 = serializeSegments(flight.itineraries[0].segments),
            flightItinerary2 = serializeSegments(flight.itineraries[1].segments),
            airlineName = airlineName,
            airlineICAOCode = airlineICAOCode,
            itinerarySummary = itinerarySummary,
        )
        if (shouldIncludeId && id != null) {
            roomTripSummary.id = id
        }
        return roomTripSummary
    }

    private fun serializeSegments(segments: List<Segment>): String {
        return segments.joinToString(":s:") { segment ->
            with(segment) {
                listOfNotNull(
                    number,
                    startTime,
                    endTime,
                    startAirport,
                    endAirport,
                    startDate,
                    endDate,
                    flightCode,
                    duration
                ).joinToString(":f:")
            }
        }
    }
}
