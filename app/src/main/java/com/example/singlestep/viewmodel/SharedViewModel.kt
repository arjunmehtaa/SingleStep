package com.example.singlestep.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singlestep.model.FlightInfo

class SharedViewModel : ViewModel() {
    private val _selectedFlight = MutableLiveData<FlightInfo>()
    val selectedFlight: LiveData<FlightInfo> = _selectedFlight

    fun selectFlight(flight: FlightInfo) {
        _selectedFlight.value = flight
    }
}
