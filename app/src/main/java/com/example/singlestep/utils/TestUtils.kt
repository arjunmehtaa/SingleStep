package com.example.singlestep.utils

import com.example.singlestep.model.Budget
import com.example.singlestep.model.Location

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
            "Dubai",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e6/Dubai_Marina_Skyline.jpg/1280px-Dubai_Marina_Skyline.jpg"
        ),
        Location(
            "London",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/London_Skyline_%28125508655%29.jpeg/1920px-London_Skyline_%28125508655%29.jpeg"
        ),
        Location(
            "Dublin",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/GoergeSalmonTrinityCollegeDublin.jpg/1280px-GoergeSalmonTrinityCollegeDublin.jpg"
        ),
        Location(
            "Tokyo",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/1920px-Skyscrapers_of_Shinjuku_2009_January.jpg"
        ),
    )
}

fun getSampleBudgets(): List<Budget> {
    return listOf(
        Budget(1000),
        Budget(5000),
        Budget(10000),
        Budget(15000),
        Budget(20000),
        Budget(50000)
    )
}