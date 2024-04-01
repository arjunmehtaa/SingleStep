package com.example.singlestep.data

import android.content.Context
import android.util.Log
import com.amadeus.android.Amadeus
import com.amadeus.android.ApiResult
import com.amadeus.android.domain.resources.Activity
import com.amadeus.android.domain.resources.Airline
import com.amadeus.android.domain.resources.FlightOfferSearch
import com.amadeus.android.domain.resources.Location
import com.example.singlestep.R
import com.example.singlestep.model.Flight
import com.example.singlestep.utils.amadeusFlightListToFlightList

class Amadeus(context: Context) {

    private val amadeus = Amadeus.Builder(context)
        .setClientId(context.getString(R.string.amadeus_api_key))
        .setClientSecret(context.getString(R.string.amadeus_api_secret))
        .setHostName(Amadeus.Builder.Hosts.PRODUCTION)
        .build()

    suspend fun getTouristAttractions(lat: Double, long: Double): List<Activity> {
        val touristAttractionList: List<Activity>
        when (val touristAttractions = amadeus.shopping.activities.get(lat, long, 15)) {
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

    private suspend fun getAirlineNamesMap(codes: List<String>): HashMap<String, Airline> {
        val airlinesMap: HashMap<String, Airline> = hashMapOf()
        when (val airlines = amadeus.referenceData.airlines.get(codes.joinToString(","))) {
            is ApiResult.Success -> {
                airlines.data.map {
                    airlinesMap.put(it.iataCode!!, it)
                }
            }

            is ApiResult.Error -> {
                Log.i("ERROR: ", airlines.toString())
                throw RuntimeException()
            }
        }
        return airlinesMap
    }

    suspend fun getFlights(
        cityDepart: String,
        cityDestination: String,
        dateDepart: String,
        dateReturn: String,
        guests: Int,
        maxPricePerGuest: Int,
    ): Pair<List<Flight>, HashMap<String, Airline>> {
        val flightList: List<FlightOfferSearch>
        when (val flights = amadeus.shopping.flightOffersSearch.get(
            cityDepart,
            cityDestination,
            dateDepart,
            guests,
            dateReturn,
            currencyCode = "CAD",
            maxPrice = maxPricePerGuest
        )) {
            is ApiResult.Success -> {
                flightList = flights.data
            }

            is ApiResult.Error -> {
                Log.i("ERROR: ", flights.toString())
                throw RuntimeException()
            }
        }
        val airlineNamesSet: MutableSet<String> = mutableSetOf()
        flightList.forEach {
            if (!it.validatingAirlineCodes.isNullOrEmpty()) {
                airlineNamesSet.addAll(it.validatingAirlineCodes!!)
            }
        }
        val airlineNamesMap = getAirlineNamesMap(airlineNamesSet.toList())
        if (flightList.size > 10) {
            return amadeusFlightListToFlightList(flightList.subList(0, 10)) to airlineNamesMap
        }
        return amadeusFlightListToFlightList(flightList) to airlineNamesMap
    }

    suspend fun getIATA(latitude: Double, longitude: Double): List<Location> {
        val airportList: List<Location>
        when (val airports = amadeus.referenceData.locations.airports.get(
            latitude,
            longitude,
            50
        )) {
            is ApiResult.Success -> {
                airportList = airports.data
            }

            is ApiResult.Error -> {
                Log.i("ERROR: ", airports.toString())
                throw RuntimeException()
            }
        }

        /*airportList.firstOrNull()?.let { airport ->
            Log.i("Airports", airport.toString())
        }*/
        return airportList
    }
}