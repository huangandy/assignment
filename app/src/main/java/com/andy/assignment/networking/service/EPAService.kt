package com.andy.assignment.networking.service

import com.andy.assignment.networking.res.AirPollutionRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EPAService {

    @GET("/api/v2/aqx_p_432")
    suspend fun getAirPollution(@Query("limit") limit: Int = 50, @Query("api_key") apiKey:String = "cebebe84-e17d-4022-a28f-81097fda5896", @Query("sort") sort:String = "ImportDate desc",@Query("format") format:String = "json"): Response<AirPollutionRes>

}