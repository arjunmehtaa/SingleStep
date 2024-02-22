package com.example.singlestep.data

import android.content.Context
import android.util.Log
import com.amadeus.android.Amadeus
import com.amadeus.android.ApiResult
import com.amadeus.android.domain.resources.Activity
import com.example.singlestep.R
import com.example.singlestep.model.AmadeusOAuthResponse
import com.example.singlestep.model.HotelOffer
import com.example.singlestep.model.HotelOfferResponse
import com.example.singlestep.model.HotelResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AmadeusApi {

    @FormUrlEncoded
    @POST("/v1/security/oauth2/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): Response<AmadeusOAuthResponse>

    @GET("/v1/reference-data/locations/hotels/by-geocode")
    suspend fun getHotelInfo(
        @Header("Authorization") auth: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double
    ): Response<HotelResponse>

    @GET("/v3/shopping/hotel-offers")
    suspend fun getHotelOffers(
        @Header("Authorization") auth: String,
        @Query("hotelIds") hotelIds: List<String>,
        @Query("priceRange") priceRange: String,
        @Query("currency") currency: String,
        @Query("adults") adults: Int,
        @Query("checkInDate") checkInDate: String,
        @Query("checkOutDate") checkOutDate: String
    ): Response<HotelOfferResponse>
}

class Amadeus(private val context: Context) {

    private val amadeus = Amadeus.Builder(context)
        .setClientId(context.getString(R.string.amadeus_api_key))
        .setClientSecret(context.getString(R.string.amadeus_api_secret))
        .build()

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(context.getString(R.string.test_amadeus_url))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    suspend fun getTouristAttractions(lat: Double, long: Double): List<Activity> {
        val touristAttractionList: List<Activity>
        when (val touristAttractions = amadeus.shopping.activities.get(lat, long)) {
            is ApiResult.Success -> {
                touristAttractionList = touristAttractions.data
            }

            is ApiResult.Error -> {
                Log.i("ERROR: ", touristAttractions.toString())
                throw RuntimeException()
            }
        }
        return touristAttractionList
    }

    suspend fun getHotelOffers(
        lat: Double,
        long: Double,
        guests: Int,
        budget: Double,
        checkInDate: String,
        checkOutDate: String
    ): List<HotelOffer> {
        val amadeusApiRetrofit = retrofit.create(AmadeusApi::class.java)

        // get client token
        val oAuthResult: Response<AmadeusOAuthResponse> = amadeusApiRetrofit.getAccessToken(
            clientId = context.getString(R.string.amadeus_api_key),
            clientSecret = context.getString(R.string.amadeus_api_secret)
        )

        var accessToken: String = ""
        if (oAuthResult.isSuccessful) {
            Log.i("DEBUG :", oAuthResult.body().toString())
            oAuthResult.body()?.let {
                accessToken = it.accessToken
            }
        } else {
            Log.i("ERROR: ", "error from OAuth API")
            Log.i("ERROR: ", oAuthResult.message())
            throw RuntimeException()
        }
        Log.i("DEBUG: ", "ACCESS TOKEN $accessToken")
        val authString = "Bearer $accessToken"

        // hotel-search API
        var hotelIdList: List<String> = listOf()
        val hotelSearchResult: Response<HotelResponse> = amadeusApiRetrofit.getHotelInfo(
            auth = authString,
            latitude = lat,
            longitude = long,
            radius = 50.0
        )
        if (hotelSearchResult.isSuccessful) {
            Log.i("DEBUG: ", hotelSearchResult.body().toString())
            hotelSearchResult.body()?.let {
                hotelIdList = it.data.map { hid ->
                    hid.hotelId
                }
            }
        } else {
            Log.i("ERROR: ", "error from list hotels API")
            Log.i("ERROR: ", hotelSearchResult.message())
            throw RuntimeException()
        }

        // hotel offers API
        var hotelOffersList: List<HotelOffer> = listOf()

        val hotelOffersResult = amadeusApiRetrofit.getHotelOffers(
            auth = authString,
            hotelIds = hotelIdList,
            priceRange = "0-${budget.toString()}",
            currency = "CAD",
            adults = guests,
            checkInDate = checkInDate,
            checkOutDate = checkOutDate
        )

        if (hotelOffersResult.isSuccessful) {
            Log.i("DEBUG: ", hotelOffersResult.body().toString())
            hotelOffersResult.body()?.let {
                hotelOffersList = it.data
            }
        } else {
            Log.i("ERROR: ", "error from hotel offers API")
            Log.i("ERROR: ", hotelOffersResult.message())
            throw RuntimeException()
        }

        return hotelOffersList
    }
}