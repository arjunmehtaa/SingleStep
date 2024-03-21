package com.example.singlestep.data

import android.content.Context
import android.util.Log
import com.example.singlestep.R
import com.example.singlestep.model.Hotel
import com.example.singlestep.model.HotelBookingResponse
import com.example.singlestep.utils.getBoundingBox
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
        @Query("checkoutDate") checkoutDate: String,
        @Query("currencyCode") currencyCode: String
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

        val results: Response<HotelBookingResponse> = tripAdvisorApiRetrofit.getHotels(
            apiKey = context.getString(R.string.booking_api_key),
            host = context.getString(R.string.booking_api_host),
            boxCoordinates[0],
            boxCoordinates[1],
            boxCoordinates[2],
            boxCoordinates[3],
            guests,
            minPrice,
            maxPrice,
            checkInDate,
            checkOutDate,
            "CAD"
        )
        if (!results.isSuccessful) {
            Log.i("ERROR: ", results.errorBody()!!.string())
        }
        val resultBody = results.body()
        if (resultBody != null) {
            return filterHotels(resultBody.data.results, maxPrice)
        }
        return listOf()
    }

    private fun filterHotels(hotels: List<Hotel>, maxPrice: Int): List<Hotel> {
        return hotels.filter {
            it.priceDisplayInfo.displayPrice.amountPerStay.amountUnformatted <= maxPrice
        }
    }

}
