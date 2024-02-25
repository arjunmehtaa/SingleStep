package com.example.singlestep.model

import java.io.Serializable

data class FlightInfo(
    var id: String = "",
    var details: TicketDetails,
    var itinerary: TicketItinerary,
    var prices: TicketPrices
)

data class TicketDetails(
    var id: Int = -1,
    var source: String = "",
    var oneWay: Boolean = false,
    var lastAccess: String = "",
    var bookableSeats: Int = -1,
    var airlineCode: String = "",
    var travelerPricing: String = ""
) : Serializable

// consider using calendar class for times
data class TicketItinerary(
    var duration: String = "",
    var departureLocations: MutableList<String> = mutableListOf(),
    var departureTimes: MutableList<String> = mutableListOf(),
    var arrivalLocations: MutableList<String> = mutableListOf(),
    var arrivalTimes: MutableList<String> = mutableListOf(),
    var carrierCodes: MutableList<String> = mutableListOf(),
    var layover: Boolean = false
) : Serializable

data class TicketPrices(
    var currency: String = "",
    var totalCost: Double = -1.0
) : Serializable
