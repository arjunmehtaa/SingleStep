package com.example.singlestep.ui.explore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amadeus.android.domain.resources.Activity
import com.example.singlestep.data.Amadeus
import com.example.singlestep.utils.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ExploreViewModel(application: Application) : AndroidViewModel(application) {
    private var coroutineExceptionHandler: CoroutineExceptionHandler

    private val _touristAttractionList: MutableLiveData<Result<List<Activity>>> = MutableLiveData()
    val touristAttractionList: LiveData<Result<List<Activity>>>
        get() = _touristAttractionList

    private val amadeus = Amadeus(application.applicationContext)

    init {
        coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            _touristAttractionList.value = Result.Failure(exception)
        }
        getTouristAttractionList()
    }

    private fun getTouristAttractionList() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _touristAttractionList.value = Result.Loading
            _touristAttractionList.value =
                    /* Ideally we would send user's current coordinates */
                Result.Success(
                    filterTouristAttractionList(
                        amadeus.getTouristAttractions(
                            41.390205,
                            2.154007
                        )
                    )
                )
        }
    }

    private fun filterTouristAttractionList(results: List<Activity>): List<Activity> {
        return results.sortedByDescending {
            it.description?.length
        }
    }
}