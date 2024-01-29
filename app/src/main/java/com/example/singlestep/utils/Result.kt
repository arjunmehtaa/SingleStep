package com.example.singlestep.utils

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val value: T) : Result<T>()
    object Loading : Result<Nothing>()
    data class Failure(val throwable: Throwable) : Result<Nothing>()
}