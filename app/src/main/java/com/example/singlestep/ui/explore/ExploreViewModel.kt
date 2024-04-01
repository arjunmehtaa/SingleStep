package com.example.singlestep.ui.explore

import android.app.Application
import android.util.Log
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
    }

    fun getTouristAttractionList(latitude: Double, longitude: Double) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _touristAttractionList.value = Result.Loading
            Log.i("DEBUG: ", "MAKING CALL TO PAID API WITH LAT $latitude, LONG $longitude");
            _touristAttractionList.value =
                    /* Ideally we would send user's current coordinates */
                Result.Success(
                    sortAndFilterTouristAttractionList(
                        amadeus.getTouristAttractions(
                            latitude,
                            longitude
                        )
                    )
                )
        }
    }

    private fun sortAndFilterTouristAttractionList(results: List<Activity>): List<Activity> {
        val filteredList = results.groupBy { it.name }
            .mapValues { (_, objects) ->
                objects.maxByOrNull { it.rating?.toDouble() ?: Double.MIN_VALUE }
            }
            .values
            .filterNotNull()
        return filteredList.sortedByDescending {
            it.description?.length
        }
    }
}