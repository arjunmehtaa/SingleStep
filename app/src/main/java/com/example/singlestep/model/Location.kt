package com.example.singlestep.model

import android.os.Parcel
import android.os.Parcelable
import com.google.android.libraries.places.api.model.PhotoMetadata

data class Location(
    val city: String,
    val imageUrl: String?,
    val latitude: Double,
    val longitude: Double,
    val photoMetadata: PhotoMetadata? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readParcelable(PhotoMetadata::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(city)
        parcel.writeString(imageUrl)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeParcelable(photoMetadata, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }
}