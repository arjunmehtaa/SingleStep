package com.example.singlestep.utils

import com.amadeus.android.domain.resources.FlightOfferSearch
import com.example.singlestep.model.FlightInfo
import com.example.singlestep.model.TicketDetails
import com.example.singlestep.model.TicketItinerary
import com.example.singlestep.model.TicketPrices

fun FlightOfferSearch.toFlightInfo(): FlightInfo {
    val itinerary = this.itineraries?.firstOrNull() // Simplification: taking the first itinerary
    val segment = itinerary?.segments?.firstOrNull() // Simplification: taking the first segment for example

    val details = TicketDetails(
        source = segment?.departure?.iataCode ?: "",
        oneWay = this.oneWay,
        lastAccess = segment?.departure?.at ?: "",
        bookableSeats = this.numberOfBookableSeats,
        airlineCode = segment?.carrierCode ?: "",
        travelerPricing = this.travelerPricings?.firstOrNull()?.price?.total?.toString() ?: "0"
    )

    val ticketItinerary = TicketItinerary(
        duration = itinerary?.duration ?: "",
        departureLocations = itinerary?.segments?.mapNotNull { it.departure?.iataCode }?.toMutableList() ?: mutableListOf(),
        departureTimes = itinerary?.segments?.mapNotNull { it.departure?.at }?.toMutableList() ?: mutableListOf(),
        arrivalLocations = itinerary?.segments?.mapNotNull { it.arrival?.iataCode }?.toMutableList() ?: mutableListOf(),
        arrivalTimes = itinerary?.segments?.mapNotNull { it.arrival?.at }?.toMutableList() ?: mutableListOf(),
        carrierCodes = itinerary?.segments?.mapNotNull { it.carrierCode }?.toMutableList() ?: mutableListOf(),
        layover = itinerary?.segments?.size ?: 0 > 1
    )

    val prices = TicketPrices(
        currency = this.price?.currency ?: "USD",
        totalCost = this.price?.total ?: 0.0
    )

    return FlightInfo(
        id = this.id ?: "",
        details = details,
        itinerary = ticketItinerary,
        prices = prices
    )
}
