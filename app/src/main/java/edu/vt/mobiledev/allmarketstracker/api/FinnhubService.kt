package edu.vt.mobiledev.allmarketstracker.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

interface FinnhubService {

    @GET("quote")
    suspend fun getQuote(
        @Query("symbol") symbol: String,
        @Query("token") apiKey: String
    ): Response<FinnhubQuote>

    @GET("stock/profile2")
    suspend fun getCompanyProfile(
        @Query("symbol") symbol: String,
        @Query("token") apiKey: String
    ): Response<FinnhubCompanyProfile>

    @GET("search")
    suspend fun searchSymbol(
        @Query("q") query: String,
        @Query("token") apiKey: String
    ): Response<FinnhubSymbolSearchResult>

    companion object {
        fun create(): FinnhubService {
            val client = OkHttpClient.Builder().build()
            return Retrofit.Builder()
                .baseUrl("https://finnhub.io/api/v1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FinnhubService::class.java)
        }
    }
}
