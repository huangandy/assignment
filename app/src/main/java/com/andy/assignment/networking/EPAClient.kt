package com.andy.assignment.networking

import com.andy.assignment.networking.res.AirPollutionRes
import com.andy.assignment.networking.service.EPAService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object EPAClient {
    private const val mEPAURL = "https://data.epa.gov.tw"
    private const val mMyNas = "https://nodered.huangtengweinas.synology.me:1881" // My testing server, do have same response(0800~2300)
    private val mEPAService: EPAService

    init {

        val client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mEPAURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        mEPAService = retrofit.create(EPAService::class.java)
    }

    //TODO::Should pass query param if needed
    suspend fun getAirPollution(): AirPollutionRes = withContext(Dispatchers.IO) {
        val response = mEPAService.getAirPollution()
        if (response.isSuccessful) {
            return@withContext response.body()!!
        } else {
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }
}