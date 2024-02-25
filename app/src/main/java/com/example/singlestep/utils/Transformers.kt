package com.example.singlestep.utils
import android.util.Log
import com.example.singlestep.model.Location
import com.google.android.libraries.places.api.model.Place

fun placeToLocation(place: Place): Location {
    Log.d("placeToLocation", "Processing place: ${place.name}, Lat: ${place.latLng?.latitude}, Lng: ${place.latLng?.longitude}")
    return Location(
        place.name ?: "Unknown", // Fallback to "Unknown" if name is null
        null, // Assuming image URL isn't available from the Place object
        place.latLng!!.latitude,
        place.latLng!!.longitude
    )
}
