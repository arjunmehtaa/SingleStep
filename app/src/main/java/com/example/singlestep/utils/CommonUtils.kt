package com.example.singlestep.utils

import android.util.Log
import java.text.NumberFormat
import java.util.Locale

fun onLoading() {
    Log.i("Loading", "onLoading()")
}

fun onLoadingFailure() {
    Log.i("Loading Failed", "onLoadingFailure()")
}

fun formatBudget(input: String): Pair<Double, String> {
    val regex = Regex("[^0-9]")
    val value = regex.replace(input.toString(), "").toDouble()
    val budgetString = NumberFormat.getInstance(Locale.CANADA).format(value)
    return value to budgetString
}