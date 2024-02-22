package com.example.singlestep.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.amadeus.android.domain.resources.Activity
import com.example.singlestep.data.Amadeus
import com.example.singlestep.model.Location
import com.example.singlestep.model.TripParameters
import com.example.singlestep.model.FlightInfo
import com.example.singlestep.utils.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class FlightViewModel(application: Application) : AndroidViewModel(application) {
    private var coroutineExceptionHandler: CoroutineExceptionHandler
    private val _flightList: MutableLiveData<Result<List<FlightInfo>>> = MutableLiveData()
    val flightList: LiveData<Result<List<FlightInfo>>>
        get() = _flightList
    private val amadeus = Amadeus(application.applicationContext)

    init {
        coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            _flightList.value = Result.Failure(exception)
        }
        getFlightAttractionList()
    }

    private fun getFlightAttractionList() {
        var mockData = TripParameters(Location("YYZ", null), Location("JFK", null), "2024-06-17 - 2024-07-01", 10000.0, 3)
        viewModelScope.launch(coroutineExceptionHandler) {
            _flightList.value = Result.Loading

            var flightResult = amadeus.getFlights(
                mockData.source.city,
                mockData.destination.city,
                mockData.dates.split(" - ")[0],
                mockData.dates.split(" - ")[1],
                mockData.guests
            )
            _flightList.value = Result.Success(flightResult)

            /*flightResult.firstOrNull()?.let { firstFlight ->
                Log.i("Flight Data: ", firstFlight.prices.toString())
            }*/
        }
    }

    private fun filterFlightList(results: List<Activity>): List<Activity> {
        return results.sortedByDescending {
            it.description?.length
        }
    }
}