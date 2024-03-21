package com.example.singlestep.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Flight(
    val id: String?,
    val airlineCode: String?,
    val rawPrice: Double = 0.0,
    val totalPrice: String,
    val itineraries: List<Itinerary>,
) : Parcelable

@Parcelize
data class Itinerary(
    val segments: List<Segment>
) : Parcelable

@Parcelize
data class Segment(
    val number: String?,
    val startTime: String,
    val endTime: String,
    val startAirport: String?,
    val endAirport: String?,
    val startDate: String,
    val endDate: String,
    val flightCode: String,
    val duration: String,
) : Parcelable