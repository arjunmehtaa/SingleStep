package com.example.singlestep.model

import java.io.Serializable

data class Location(
    val city: String,
    val imageUrl: String?
) : Serializable