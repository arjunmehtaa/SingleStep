package com.example.singlestep.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.EditText
import com.example.singlestep.model.Budget
import com.example.singlestep.model.Location
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getSampleDestinations(): List<Location> {
    return listOf(
        Location(
            "Toronto",
            "https://upload.wikimedia.org/wikipedia/commons/a/a8/CC_2022-06-18_193-Pano_%28cropped%29_01.jpg"
        ),
        Location(
            "New York",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/New_york_times_square-terabass.jpg/450px-New_york_times_square-terabass.jpg"
        ),
        Location(
            "Paris",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/La_Tour_Eiffel_vue_de_la_Tour_Saint-Jacques%2C_Paris_ao%C3%BBt_2014_%282%29.jpg/1280px-La_Tour_Eiffel_vue_de_la_Tour_Saint-Jacques%2C_Paris_ao%C3%BBt_2014_%282%29.jpg"
        ),
        Location(
            "Waterloo",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/University_of_Waterloo_William_G._Davis_Computer_Research_Center.jpg/1280px-University_of_Waterloo_William_G._Davis_Computer_Research_Center.jpg"
        ),
        Location(
            "London",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/London_Skyline_%28125508655%29.jpeg/1920px-London_Skyline_%28125508655%29.jpeg"
        ),
        Location(
            "Dublin",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/GoergeSalmonTrinityCollegeDublin.jpg/1280px-GoergeSalmonTrinityCollegeDublin.jpg"
        )
    )
}

fun getSampleBudgets(): List<Budget> {
    return listOf(
        Budget(1000),
        Budget(5000),
        Budget(10000),
        Budget(20000)
    )
}

fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
    isFocusableInTouchMode = false
    isClickable = true
    isFocusable = false

    val myCalendar = Calendar.getInstance()
    val datePickerOnDataSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat(format, Locale.UK)
            setText(sdf.format(myCalendar.time))
        }

    setOnClickListener {
        DatePickerDialog(
            context, datePickerOnDataSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).run {
            maxDate?.time?.also { datePicker.maxDate = it }
            show()
        }
    }
}