package com.example.singlestep.model

import android.graphics.Bitmap
import android.os.Parcelable
import com.google.android.libraries.places.api.model.PhotoMetadata
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val city: String,
    val imageUrl: String?,
    val latitude: Double,
    val longitude: Double,
    var photoMetadata: PhotoMetadata? = null,
    var imageBitmap: Bitmap? = null
) : Parcelable