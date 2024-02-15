package com.example.singlestep.model

import java.io.Serializable

data class TripParameters(
    val source: Location,
    val destination: Location,
    val dates: String,
    val budget: String,
    val guests: String
) : Serializable