package com.example.singlestep.model

import java.io.Serializable

data class TripParameters(
    val destination: String,
    val dates: String,
    val budget: String,
    val guests: String
) : Serializable