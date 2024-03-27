package com.example.singlestep.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AssistantRequest(
    @SerializedName("trip_location") val tripLocation: String,
    @SerializedName("arrival_time") val arrivalTime: String,
    @SerializedName("departure_time") val departureTime: String,
    @SerializedName("hotel_address") val hotelAddress: String,
    @SerializedName("total_budget") val totalBudget: Double,
) : Parcelable
