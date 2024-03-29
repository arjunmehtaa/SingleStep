package com.example.singlestep.data

import android.util.Log
import com.example.singlestep.model.AssistantRequest
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface AssistantApi {
    @POST("/generate-itinerary-freeform")
    suspend fun getItineraryString(
        @Body getItineraryStringParams: AssistantRequest
    ): Response<String>
}

class Assistant {
    // using localhost for now, change if deployed
    // needs additional ScalarsConverterFactory to convert raw HTML response

    private val client = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080")
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    suspend fun getItineraryString(
        tripLocation: String,
        arrivalTime: String,
        departureTime: String,
        hotelAddress: String,
        totalBudget: Double,
    ): String {
        val assistantRetrofit = retrofit.create(AssistantApi::class.java)
        val requestBody = AssistantRequest(
            tripLocation = tripLocation,
            arrivalTime = arrivalTime,
            departureTime = departureTime,
            hotelAddress = hotelAddress,
            totalBudget = totalBudget
        )
        val results: Response<String> = assistantRetrofit.getItineraryString(requestBody)
        if (!results.isSuccessful) {
            Log.i("ERROR: ", results.errorBody()!!.string())
        }

        // replace some weird additional strings provided
        return results.body()!!.replace("```html", "").replace("```", "")
    }
}