package com.hana.eventapp.data.retrofit

import com.hana.eventapp.data.response.DetailResponse
import com.hana.eventapp.data.response.ListEventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/events/{id}")
    fun getEventDetail(
        @Path("id") id: String
    ): Call<DetailResponse>

    @GET("/events")
    fun getUpcoming(
        @Query("active") active: Int = 1,
    ): Call<ListEventResponse>

    @GET("/events")
    fun getFinished(
        @Query("active") active: Int = 0,
    ): Call<ListEventResponse>

}


