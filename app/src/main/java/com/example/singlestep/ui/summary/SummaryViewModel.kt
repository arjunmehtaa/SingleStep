package com.example.singlestep.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SummaryViewModel : ViewModel() {
    private val _summaryInfo = MutableLiveData<String>()
    val summaryInfo: LiveData<String> = _summaryInfo

    init {
        // Load or generate summary information as needed
        loadSummaryInfo()
    }

    private fun loadSummaryInfo() {
        _summaryInfo.value = "Summary of the trip details goes here."
    }

}
