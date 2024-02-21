package com.example.singlestep.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.singlestep.data.Amadeus
import com.example.singlestep.model.HotelOffer
import com.example.singlestep.model.TripParameters
import com.example.singlestep.utils.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch


class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private var coroutineExceptionHandler: CoroutineExceptionHandler

    private val _hotelOffersList: MutableLiveData<Result<List<HotelOffer>>> = MutableLiveData()

    private val amadeus = Amadeus(application.applicationContext)


    val hotelOffersList: LiveData<Result<List<HotelOffer>>>
        get() = _hotelOffersList

    init {
        coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            _hotelOffersList.value = Result.Failure(exception)
        }
    }

    fun getHotelOffersList(tripParameters: TripParameters) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _hotelOffersList.value = Result.Loading
            // TO-DO: Pass in checkInDate and checkOutDate from trip parameters
            // pass in lat, long from trip parameters (but google places API seems to return as null)
            _hotelOffersList.value = Result.Success(
                filterHotelOffersList(
                    amadeus.getHotelOffers(
                        41.2324,
                        2.9144,
                        tripParameters.guests,
                        tripParameters.budget,
                        "2024-04-01",
                        "2024-04-05"
                    )
                )
            )

        }
    }

    private fun filterHotelOffersList(results: List<HotelOffer>): List<HotelOffer> {
        return results.filter {
            it.available
        }
    }



}