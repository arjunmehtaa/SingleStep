package com.example.singlestep.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TripParameters(
    val source: Location,
    val destination: Location,
    val checkInDate: String,
    val checkOutDate: String,
    val budget: Double,
    val guests: Int
) : Parcelable