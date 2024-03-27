package com.example.singlestep.ui.hotels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.singlestep.data.Booking
import com.example.singlestep.model.Hotel
import com.example.singlestep.model.TripParameters
import com.example.singlestep.utils.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class HotelViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private var coroutineExceptionHandler: CoroutineExceptionHandler
    private val booking = Booking(application.applicationContext)
    private val tripParameters =
        savedStateHandle.getLiveData<TripParameters>("tripParameters").value!!

    private val _hotelList: MutableLiveData<Result<List<Hotel>>> = MutableLiveData()
    val hotelList: LiveData<Result<List<Hotel>>>
        get() = _hotelList

    init {
        coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            _hotelList.value = Result.Failure(exception)
        }
        getHotels()
    }

    fun getHotels() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _hotelList.postValue(Result.Loading)
            try {
                val hotels = booking.getHotels(
                    tripParameters.destination.latitude,
                    tripParameters.destination.longitude,
                    tripParameters.checkInDate.replace("/", "-"),
                    tripParameters.checkOutDate.replace("/", "-"),
                    0,
                    tripParameters.remainingBudget.toInt(),
                    tripParameters.guests
                )
//                Log.d("HotelViewModel", "Fetched hotels: $hotels")
                _hotelList.postValue(Result.Success(hotels))
            } catch (e: Exception) {
                Log.e("HotelViewModel", "Error fetching hotels", e)
                _hotelList.postValue(Result.Failure(e))
            }
        }
    }
}
