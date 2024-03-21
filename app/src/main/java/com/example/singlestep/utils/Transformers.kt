package com.example.singlestep.utils

import com.amadeus.android.domain.resources.FlightOfferSearch
import com.example.singlestep.model.Flight
import com.example.singlestep.model.Itinerary
import com.example.singlestep.model.Location
import com.example.singlestep.model.RoomTripSummary
import com.example.singlestep.model.Segment
import com.example.singlestep.model.TripSummary
import com.google.android.libraries.places.api.model.Place

fun placeToLocation(place: Place): Location {
    return Location(
        place.name ?: "Unknown", // Fallback to "Unknown" if name is null
        null, place.latLng!!.latitude, place.latLng!!.longitude, place.photoMetadatas?.first()
    )
}

fun amadeusFlightListToFlightList(flightOfferSearchList: List<FlightOfferSearch>): List<Flight> {
    val flights: MutableList<Flight> = mutableListOf()
    flightOfferSearchList.forEach {
        flights.add(
            Flight(
                id = it.id,
                airlineCode = it.validatingAirlineCodes?.get(0),
                rawPrice = it.price?.grandTotal ?: 0.0,
                totalPrice = "${it.price?.currency} ${it.price?.grandTotal?.toInt()}",
                itineraries = amadeusItineraryListToItineraryList(it.itineraries)
            )
        )
    }
    return flights
}

fun amadeusItineraryListToItineraryList(amadeusItineraries: List<FlightOfferSearch.Itinerary>?): List<Itinerary> {
    val itineraries: MutableList<Itinerary> = mutableListOf()
    amadeusItineraries?.forEach { itinerary ->
        itineraries.add(Itinerary(amadeusSegmentListToSegmentList(itinerary.segments)))
    }
    return itineraries
}

fun amadeusSegmentListToSegmentList(amadeusSegments: List<FlightOfferSearch.SearchSegment>?): List<Segment> {
    val segments: MutableList<Segment> = mutableListOf()
    amadeusSegments?.forEach { segment ->
        val startTimeText = segment.departure?.at ?: "MMMM DD, YYYYTXX:XX"
        val endTimeText = segment.arrival?.at ?: "MMMM DD, YYYYTXX:XX"
        segments.add(
            Segment(
                number = segment.number,
                startTime = getFormattedTime(
                    startTimeText.substring(
                        startTimeText.indexOf("T") + 1, startTimeText.length - 3
                    )
                ),
                endTime = getFormattedTime(
                    endTimeText.substring(
                        endTimeText.indexOf("T") + 1, endTimeText.length - 3
                    )
                ),
                startAirport = segment.departure?.iataCode,
                endAirport = segment.arrival?.iataCode,
                startDate = getDateAndMonthName(
                    startTimeText.substring(
                        0, startTimeText.indexOf("T")
                    )
                ),
                endDate = getDateAndMonthName(
                    endTimeText.substring(
                        0, endTimeText.indexOf("T")
                    )
                ),
                flightCode = "${segment.carrierCode} ${segment.number}",
                duration = formatDuration(segment.duration),
            )
        )
    }
    return segments
}

fun roomTripSummaryListToTripSummaryList(roomTripSummaryList: List<RoomTripSummary>): List<TripSummary> {
    return roomTripSummaryList.map {
        it.toTripSummary()
    }
}
