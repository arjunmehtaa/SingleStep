package com.example.singlestep.utils

import androidx.core.util.Pair
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
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
    val month = String.format("%02d", utc.get(Calendar.MONTH) + 1);
    val day = String.format("%02d", utc.get(Calendar.DAY_OF_MONTH));
    return "${utc.get(Calendar.YEAR)}/${month}/${day}"
}

fun getDaysBetween(startDateString: String, endDateString: String): Long {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val startDate = LocalDate.parse(startDateString, formatter)
    val endDate = LocalDate.parse(endDateString, formatter)
    return ChronoUnit.DAYS.between(startDate, endDate) + 1
}