package com.example.singlestep.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.singlestep.data.room.AppDatabase
import com.example.singlestep.model.RoomTripSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SummaryViewModel(
    application: Application
) : AndroidViewModel(application) {
    private var dao = AppDatabase.getDatabase(application.applicationContext).tripSummaryDao()

    private val _summaryInfo = MutableLiveData<String>()
    val summaryInfo: LiveData<String> = _summaryInfo

    init {
        // Load or generate summary information as needed
        loadSummaryInfo()
    }

    private fun loadSummaryInfo() {
        _summaryInfo.value = "Summary of the trip details goes here."
    }

    fun saveToRoomDatabase(roomTripSummary: RoomTripSummary) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertAll(roomTripSummary)
        }
    }

    fun removeFromRoomDatabase(roomTripSummary: RoomTripSummary) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(roomTripSummary)
        }
    }

}
