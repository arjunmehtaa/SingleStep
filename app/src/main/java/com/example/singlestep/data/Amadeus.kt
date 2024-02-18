package com.example.singlestep.data

import android.content.Context
import android.util.Log
import com.amadeus.android.Amadeus
import com.amadeus.android.ApiResult
import com.amadeus.android.domain.resources.Activity
import com.example.singlestep.R

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
}