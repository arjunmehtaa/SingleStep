package com.example.singlestep.utils

import com.example.singlestep.model.Location
import com.google.android.libraries.places.api.model.Place

fun placeToLocation(place: Place): Location {
    return Location(
        place.name ?: "Unknown", // Fallback to "Unknown" if name is null
        null, // Assuming image URL isn't available from the Place object
        place.latLng!!.latitude,
        place.latLng!!.longitude
    )
}
