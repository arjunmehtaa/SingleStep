package com.example.singlestep.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.singlestep.model.Item
import com.example.singlestep.utils.Result
import com.example.singlestep.utils.getSampleItems
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ResultViewModel : ViewModel() {
    private var coroutineExceptionHandler: CoroutineExceptionHandler

    private val _itemList: MutableLiveData<Result<List<Item>>> = MutableLiveData()
    val itemList: LiveData<Result<List<Item>>>
        get() = _itemList

    init {
        coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            _itemList.value = Result.Failure(exception)
        }
        getItemList()
    }

    private fun getItemList() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _itemList.value = Result.Loading
            // In the actual code, we would make an API request here to fetch some data
            _itemList.value = Result.Success(getSampleItems())
        }
    }
}