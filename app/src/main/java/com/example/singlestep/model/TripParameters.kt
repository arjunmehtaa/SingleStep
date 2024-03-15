package com.example.singlestep.model

import android.os.Parcel
import android.os.Parcelable

data class TripParameters(
    val source: Location,
    val destination: Location,
    val checkInDate: String,
    val checkOutDate: String,
    val budget: Double,
    val guests: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Location::class.java.classLoader)!!,
        parcel.readParcelable(Location::class.java.classLoader)!!,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(source, flags)
        parcel.writeParcelable(destination, flags)
        parcel.writeString(checkInDate)
        parcel.writeString(checkOutDate)
        parcel.writeDouble(budget)
        parcel.writeInt(guests)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TripParameters> {
        override fun createFromParcel(parcel: Parcel): TripParameters {
            return TripParameters(parcel)
        }

        override fun newArray(size: Int): Array<TripParameters?> {
            return arrayOfNulls(size)
        }
    }
}