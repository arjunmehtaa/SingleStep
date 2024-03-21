package com.example.singlestep.ui.flights

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.amadeus.android.domain.resources.Airline
import com.example.singlestep.data.Amadeus
import com.example.singlestep.model.Flight
import com.example.singlestep.model.TripParameters
import com.example.singlestep.utils.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class FlightViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private var coroutineExceptionHandler: CoroutineExceptionHandler
    private val amadeus = Amadeus(application.applicationContext)
    private val tripParameters =
        savedStateHandle.getLiveData<TripParameters>("tripParameters").value!!

    private val _flightList: MutableLiveData<Result<List<Flight>>> = MutableLiveData()
    val flightList: LiveData<Result<List<Flight>>>
        get() = _flightList

    private val _airlineNamesMap: MutableLiveData<Result<HashMap<String, Airline>>> =
        MutableLiveData()
    val airlineNamesMap: LiveData<Result<HashMap<String, Airline>>>
        get() = _airlineNamesMap

    init {
        coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            _flightList.value = Result.Failure(exception)
        }
        getFlightAttractionList()
    }

    fun getFlightAttractionList() {
        /*var mockData = TripParameters(
            Location("YYZ", null, 0.0, 0.0),
            Location("JFK", null, 0.0, 0.0),
            "2024/06/17",
            "2024/07/01",
            10000.0,
            3
        )*/

//        Log.i("trip parameters: ", tripParameters.toString())
        viewModelScope.launch(coroutineExceptionHandler) {
            _flightList.value = Result.Loading

            val airportDepart = amadeus.getIATA(
                tripParameters.source.latitude,
                tripParameters.source.longitude
            )

            val airportArrive = amadeus.getIATA(
                tripParameters.destination.latitude,
                tripParameters.destination.longitude
            )
//            Log.i("depart: ", airportDepart.toString())
//            Log.i("arrival: ", airportArrive.toString())

            if (airportDepart.isEmpty() || airportArrive.isEmpty()) {
                _flightList.value = Result.Success(listOf())
            } else {
                val flightResult = amadeus.getFlights(
                    airportDepart[0].iataCode.toString(),
                    airportArrive[0].iataCode.toString(),
                    tripParameters.checkInDate.replace("/", "-"),
                    tripParameters.checkOutDate.replace("/", "-"),
                    tripParameters.guests,
                    maxPricePerGuest = tripParameters.originalBudget.div(tripParameters.guests)
                        .toInt()
                )

                _flightList.value = Result.Success(flightResult.first)
                _airlineNamesMap.value = Result.Success(flightResult.second)
            }
        }
    }

}