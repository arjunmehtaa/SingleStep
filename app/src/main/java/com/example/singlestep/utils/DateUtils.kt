package com.example.singlestep.utils

import androidx.core.util.Pair
import java.util.Calendar
import java.util.TimeZone

fun getSelectedDateRange(selection: Pair<Long, Long>): String {
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utc.timeInMillis = selection.first
    val startDateString = getDateString(utc)
    utc.timeInMillis = selection.second
    val endDateString = getDateString(utc)
    return "$startDateString - $endDateString"
}

fun getDateString(utc: Calendar): String {
    return "${utc.get(Calendar.DAY_OF_MONTH)}/${utc.get(Calendar.MONTH) + 1}/${utc.get(Calendar.YEAR)}"
}