package com.example.singlestep.data

import android.content.Context
import android.util.Log
import com.amadeus.android.Amadeus
import com.amadeus.android.ApiResult
import com.amadeus.android.domain.resources.Activity
import com.amadeus.android.domain.resources.FlightOfferSearch
import com.example.singlestep.R
import com.example.singlestep.model.FlightInfo
import com.example.singlestep.model.TicketDetails
import com.example.singlestep.model.TicketItinerary
import com.example.singlestep.model.TicketPrices

class Amadeus(context: Context) {

    private val amadeus = Amadeus.Builder(context)
        .setClientId(context.getString(R.string.amadeus_api_key))
        .setClientSecret(context.getString(R.string.amadeus_api_secret))
        .build()

    suspend fun getTouristAttractions(lat: Double, long: Double): List<Activity> {
        val touristAttractionList: List<Activity>
        when (val touristAttractions = amadeus.shopping.activities.get(lat, long)) {
            is ApiResult.Success -> {
                touristAttractionList = touristAttractions.data
            }

            is ApiResult.Error -> {
                Log.i("ERROR: ", touristAttractions.toString())
                throw RuntimeException()
            }
        }
        return touristAttractionList
    }

    suspend fun getFlights(
        cityDepart: String,
        cityDestination: String,
        dateDepart: String,
        dateReturn: String,
        guests: Int
    ): List<FlightInfo> {
        val flightList: List<FlightOfferSearch>
        when (val flights = amadeus.shopping.flightOffersSearch.get(
            cityDepart,
            cityDestination,
            dateDepart,
            guests,
            dateReturn
        )) {
            is ApiResult.Success -> {
                flightList = flights.data
            }

            is ApiResult.Error -> {
                Log.i("ERROR: ", flights.toString())
                throw RuntimeException()
            }
        }

        var flightListInfo = mutableListOf<FlightInfo>()
        var prevID = 0
        flightList.forEach { result ->
            val flightInfo = parseFlightInfo(result, prevID)
            prevID = flightInfo.last().id.toInt()
            flightListInfo.addAll(flightInfo)
        }

        for (flight in flightListInfo) {
            Log.i("Flight IDS: ", flight.id)
        }
        return flightListInfo
    }

    private fun parseFlightInfo(flightInfo: FlightOfferSearch, prevID: Int): List<FlightInfo> {
        var flightsInfo = mutableListOf<FlightInfo>()
        var currTicketDetails = TicketDetails()
        var currTicketPrice = TicketPrices()
        var i = 1

        currTicketDetails.id = flightInfo.id!!.toInt()
        currTicketDetails.source = flightInfo.source.toString()
        currTicketDetails.oneWay = flightInfo.oneWay
        currTicketDetails.lastAccess = flightInfo.lastTicketingDate.toString()
        currTicketDetails.bookableSeats = flightInfo.numberOfBookableSeats
        currTicketDetails.airlineCode = flightInfo.validatingAirlineCodes.toString()
        currTicketDetails.travelerPricing = flightInfo.travelerPricings.toString()

        currTicketPrice.currency = flightInfo?.price!!.currency.toString()
        currTicketPrice.totalCost = flightInfo?.price!!.grandTotal

        var currItinerary = TicketItinerary()
        flightInfo.itineraries?.forEach { itinerary ->
            itinerary.segments?.forEach { segment ->
                currItinerary.duration = segment.duration.toString()
                currItinerary.departureLocations.add(segment.departure!!.iataCode.toString())
                currItinerary.arrivalLocations.add(segment.arrival!!.iataCode.toString())
                currItinerary.departureTimes.add(segment.departure!!.at.toString())
                currItinerary.arrivalTimes.add(segment.arrival!!.at.toString())
                currItinerary.carrierCodes.add(segment.carrierCode.toString())
            }
            if (currItinerary.departureLocations.size > 1) {
                currItinerary.layover = true
            }

            flightsInfo.add(
                FlightInfo(
                    (prevID + i).toString(),
                    currTicketDetails,
                    currItinerary,
                    currTicketPrice
                )
            )
            i++
        }
        return flightsInfo
    }
}