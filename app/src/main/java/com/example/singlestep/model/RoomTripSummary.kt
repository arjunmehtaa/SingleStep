package com.example.singlestep.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomTripSummary(

    /* Flattened Trip Parameters */
    val sourceCity: String,
    val sourceImageUrl: String?,
    val sourceLatitude: Double,
    val sourceLongitude: Double,
    val destinationCity: String,
    val destinationImageUrl: String?,
    val destinationLatitude: Double,
    val destinationLongitude: Double,
    val cityImageFileUri: String? = null,
    val checkInDate: String,
    val checkOutDate: String,
    val originalBudget: Double,
    val remainingBudget: Double,
    val guests: Int,

    /* Flattened Hotel */
    val hotelName: String,
    val hotelCurrency: String,
    val hotelAmountUnformatted: Double,
    val hotelAmountRounded: String,
    val hotelCurrencyBeforeDiscount: String,
    val hotelAmountUnformattedBeforeDiscount: Double,
    val hotelAmountRoundedBeforeDiscount: String,
    val hotelPhotoUrl: String,
    val hotelAddress: String,
    val hotelScore: Double,
    val hotelScoreTag: String?,

    /* Flattened Flight */
    val flightId: String?,
    val flightAirlineCode: String?,
    val flightRawPrice: Double,
    val flightTotalPrice: String,
    val flightItinerary1: String,
    val flightItinerary2: String,

    val airlineName: String,
    val airlineICAOCode: String,
    val itinerarySummary: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun toTripSummary(): TripSummary {
        return TripSummary(
            id = id,
            tripParameters = TripParameters(
                source = Location(
                    city = sourceCity,
                    imageUrl = sourceImageUrl,
                    latitude = sourceLatitude,
                    longitude = sourceLongitude,
                    imageFileUri = null
                ),
                destination = Location(
                    city = destinationCity,
                    imageUrl = destinationImageUrl,
                    latitude = destinationLatitude,
                    longitude = destinationLongitude,
                    imageFileUri = cityImageFileUri
                ),
                checkInDate = checkInDate,
                checkOutDate = checkOutDate,
                originalBudget = originalBudget,
                remainingBudget = remainingBudget,
                guests = guests
            ),
            hotel = Hotel(
                displayName = DisplayName(hotelName),
                priceDisplayInfo = PriceDisplayInfo(
                    displayPrice = DisplayPrice(
                        AmountPerStay(
                            currency = hotelCurrency,
                            amountUnformatted = hotelAmountUnformatted,
                            amountRounded = hotelAmountRounded
                        )
                    ),
                    priceBeforeDiscount = PriceBeforeDiscount(
                        AmountPerStay(
                            currency = hotelCurrencyBeforeDiscount,
                            amountUnformatted = hotelAmountUnformattedBeforeDiscount,
                            amountRounded = hotelAmountRoundedBeforeDiscount
                        )
                    )
                ),
                basicPropertyData = BasicPropertyData(
                    photos = Photo(
                        MainPhoto(
                            AbsoluteUrl(hotelPhotoUrl)
                        )
                    ),
                    location = HotelLocation(hotelAddress),
                    reviews = Reviews(
                        totalScore = hotelScore,
                        totalScoreTextTag = TotalScoreTextTag(hotelScoreTag)
                    )
                )
            ),
            flight = Flight(
                id = flightId,
                airlineCode = flightAirlineCode,
                rawPrice = flightRawPrice,
                totalPrice = flightTotalPrice,
                itineraries = listOf(
                    Itinerary(deserializeSegments(flightItinerary1)),
                    Itinerary(deserializeSegments(flightItinerary2))
                )
            ),
            airlineName = airlineName,
            airlineICAOCode = airlineICAOCode,
            itinerarySummary = itinerarySummary,
        )
    }

    private fun deserializeSegments(serializedString: String): List<Segment> {
        val segmentStrings = serializedString.split(":s:")
        return segmentStrings.map { segmentString ->
            val fields = segmentString.split(":f:")
            Segment(
                number = fields[0].ifEmpty { null },
                startTime = fields[1],
                endTime = fields[2],
                startAirport = fields[3].ifEmpty { null },
                endAirport = fields[4].ifEmpty { null },
                startDate = fields[5],
                endDate = fields[6],
                flightCode = fields[7],
                duration = fields[8]
            )
        }
    }
}
