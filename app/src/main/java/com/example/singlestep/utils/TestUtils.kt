package com.example.singlestep.utils

import android.text.Html
import android.text.Spanned
import com.example.singlestep.model.Budget
import com.example.singlestep.model.Location

fun getSampleDestinations(): List<Location> {
    return listOf(
        Location(
            "Toronto",
            "https://upload.wikimedia.org/wikipedia/commons/a/a8/CC_2022-06-18_193-Pano_%28cropped%29_01.jpg",
            43.651070,
            -79.347015
        ),
        Location(
            "New York",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/New_york_times_square-terabass.jpg/450px-New_york_times_square-terabass.jpg",
            40.730610,
            -73.935242
        ),
        Location(
            "Paris",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/La_Tour_Eiffel_vue_de_la_Tour_Saint-Jacques%2C_Paris_ao%C3%BBt_2014_%282%29.jpg/1280px-La_Tour_Eiffel_vue_de_la_Tour_Saint-Jacques%2C_Paris_ao%C3%BBt_2014_%282%29.jpg",
            48.864716,
            2.349014
        ),
        Location(
            "Dubai",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e6/Dubai_Marina_Skyline.jpg/1280px-Dubai_Marina_Skyline.jpg",
            25.276987,
            55.296249
        ),
        Location(
            "London",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/London_Skyline_%28125508655%29.jpeg/1920px-London_Skyline_%28125508655%29.jpeg",
            51.509865,
            -0.118092
        ),
        Location(
            "Dublin",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/GoergeSalmonTrinityCollegeDublin.jpg/1280px-GoergeSalmonTrinityCollegeDublin.jpg",
            53.350140,
            -6.266155
        ),
        Location(
            "Tokyo",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/1920px-Skyscrapers_of_Shinjuku_2009_January.jpg",
            35.652832,
            139.839478
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

fun getSampleAIResponse(): Spanned {
    return Html.fromHtml(
        "<h2>Day 1: Exploring Times Square</h2>\n" +
                "<p>Upon arrival in New York at 7:35 am, settle into Pod Times Square. Grab breakfast at a local cafe, budgeting \$20 for two. Spend the morning exploring iconic Times Square, soaking in the vibrant atmosphere and taking memorable photos. Enjoy lunch at a nearby eatery, allocating \$30 for both guests. In the afternoon, visit Madame Tussauds Wax Museum, costing \$60 for two tickets. In the evening, savor dinner at a cozy restaurant, spending \$50.</p>\n" +
                "\n" +
                "<h2>Day 2: Central Park and Museums</h2>\n" +
                "<p>Start the day with a leisurely stroll through Central Park, admiring its beauty and serenity. Rent bicycles for a couple of hours, budgeting \$40 for both guests. Afterward, head to the Museum of Modern Art (MoMA), with admission costing \$50 for two. Enjoy lunch at a cafe nearby, allocating \$30. Spend the afternoon exploring the museum's remarkable collections. In the evening, indulge in a Broadway show, budgeting \$200 for two tickets.</p>\n" +
                "\n" +
                "<h2>Day 3: Brooklyn Bridge and Farewell</h2>\n" +
                "<p>Begin the day with a visit to the iconic Brooklyn Bridge, enjoying picturesque views of the city skyline. Walk across the bridge, soaking in the panoramic scenery. Budget \$20 for a light brunch in Brooklyn. Return to Manhattan and visit the One World Observatory, spending \$70 for two tickets to enjoy breathtaking views. Enjoy a farewell dinner at a local restaurant, allocating \$60. Head back to the hotel, collect belongings, and depart for the airport by 9:30 pm, utilizing \$30 for transportation. </p>\n" +
                "\n" +
                "<p>After accounting for all planned activities and meals, you would have \$90 remaining for miscellaneous expenses and shopping.</p>"
    )
}