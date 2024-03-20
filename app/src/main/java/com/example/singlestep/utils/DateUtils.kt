package com.example.singlestep.utils

import androidx.core.util.Pair
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
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

fun getDateAndMonthName(dateString: String): String {
    val parsedDate = LocalDate.parse(dateString)
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM", Locale.ENGLISH)
    return parsedDate.format(outputFormatter).substring(0, 6)
}

fun getFormattedDate(inputDate: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.ENGLISH)
    val date = LocalDate.parse(inputDate, formatter)
    val dayOfMonth = date.dayOfMonth
    val month = date.month
    return "${
        month.toString().substring(0, 3).lowercase().replaceFirstChar { it.uppercase() }
    } $dayOfMonth"
}

fun getFormattedTime(timeString: String): String {
    val parsedTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"))
    val outputFormatter = DateTimeFormatter.ofPattern("hh:mm a")
    val updatedString = parsedTime.format(outputFormatter)
    return updatedString.replace(".", "").uppercase()
}

fun formatDuration(durationString: String?): String {
    val duration = Duration.parse(durationString)
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60
    val formattedDuration = buildString {
        if (hours > 0) {
            append("${hours}h ")
        }
        if (minutes > 0) {
            append("${minutes}m")
        }
    }
    return formattedDuration.trim()
}