package com.example.singlestep.ui.mytrips

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.singlestep.data.room.AppDatabase
import com.example.singlestep.model.RoomTripSummary
import com.example.singlestep.model.TripSummary
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.roomTripSummaryListToTripSummaryList
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyTripsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var coroutineExceptionHandler: CoroutineExceptionHandler
    private var dao = AppDatabase.getDatabase(application.applicationContext).tripSummaryDao()

    private val _tripSummaryList: MutableLiveData<Result<List<TripSummary>>> = MutableLiveData()
    val tripSummaryList: LiveData<Result<List<TripSummary>>>
        get() = _tripSummaryList

    init {
        coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            _tripSummaryList.value = Result.Failure(exception)
        }
        getTripSummaryList()
    }

    fun getTripSummaryList() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _tripSummaryList.postValue(Result.Loading)
            _tripSummaryList.postValue(Result.Success(roomTripSummaryListToTripSummaryList(dao.getAll())))
        }
    }

    fun removeFromRoomDatabase(roomTripSummary: RoomTripSummary) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(roomTripSummary)
            getTripSummaryList()
        }
    }
}
