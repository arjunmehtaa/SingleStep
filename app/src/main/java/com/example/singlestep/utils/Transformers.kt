package com.example.singlestep.utils

import com.example.singlestep.model.Location
import com.google.android.libraries.places.api.model.Place

fun placeToLocation(place: Place): Location {
    var lat = 0.0
    var long = 0.0
    place.latLng?.let {
        lat = it.latitude
        long = it.longitude
    }
    return Location(
        place.name,
        null,
        lat,
        long
    )
}