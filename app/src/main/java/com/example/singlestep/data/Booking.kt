package com.example.singlestep.data

import android.content.Context
import android.util.Log
import com.example.singlestep.model.Hotel
import com.example.singlestep.model.HotelBookingResponse
import getBoundingBox
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BookingApi {
    @GET("/stays/search-by-geo")
    suspend fun getHotels(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") host: String,
        @Query("swLat") swLat: Double,
        @Query("swLng") swLng: Double,
        @Query("neLat") neLat: Double,
        @Query("neLng") neLng: Double,
        @Query("adults") adults: Int,
        @Query("minPrice") minPrice: Int,
        @Query("maxPrice") maxPrice: Int,
        @Query("checkinDate") checkinDate: String,
        @Query("checkoutDate") checkoutDate: String
    ): Response<HotelBookingResponse>
}

class Booking(private val context: Context) {
    private val retrofit = Retrofit.Builder().baseUrl("https://booking-com18.p.rapidapi.com")
        .addConverterFactory(GsonConverterFactory.create()).build()

    suspend fun getHotels(
        latitude: Double,
        longitude: Double,
        checkInDate: String,
        checkOutDate: String,
        minPrice: Int,
        maxPrice: Int,
        guests: Int
    ): List<Hotel> {

        val tripAdvisorApiRetrofit = retrofit.create(BookingApi::class.java)
        val boxCoordinates = getBoundingBox(Pair(latitude, longitude), 5.0)

        Log.i("Lat, long is: ", latitude.toString() + ", " + longitude.toString())

        val results: Response<HotelBookingResponse> = tripAdvisorApiRetrofit.getHotels(
            apiKey = "35ba2fe984msh47c31ab84324ed7p143d9ejsne0b6c2e4c472",
            host = "booking-com18.p.rapidapi.com",
            boxCoordinates[0],
            boxCoordinates[1],
            boxCoordinates[2],
            boxCoordinates[3],
            guests,
            minPrice,
            maxPrice,
            checkInDate,
            checkOutDate
        )
        if (results.isSuccessful) {
            Log.i("DEBUG: ", results.body().toString())
        } else {
            Log.i("ERROR: ", "error from hotel offers API")
            Log.i("ERROR: ", results.errorBody()!!.string())
        }
        Log.i("AO:", results.body()!!.data.results.toString())
        return results.body()!!.data.results
    }

}
