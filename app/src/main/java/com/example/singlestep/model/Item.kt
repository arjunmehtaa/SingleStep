package com.example.singlestep.model

import java.io.Serializable

data class Item(
    val id: Int,
    val name: String,
    val description: String,
) : Serializable