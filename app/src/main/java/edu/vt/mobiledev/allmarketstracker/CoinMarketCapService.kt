package edu.vt.mobiledev.allmarketstracker

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import edu.vt.mobiledev.allmarketstracker.api.CoinMarketCapResponse
import edu.vt.mobiledev.allmarketstracker.api.CryptoInfoResponse


interface CoinMarketCapService {
    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getLatestListings(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 100,
        @Query("convert") convert: String = "USD"
    ): Response<CoinMarketCapResponse>

    @GET("v2/cryptocurrency/info")
    suspend fun getCryptoInfo(
        @Query("id") ids: String
    ): Response<CryptoInfoResponse>

    companion object {
        fun create(): CoinMarketCapService {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("X-CMC_PRO_API_KEY", "cab73baa-e321-4f56-8ad3-1f7b543621dd")
                        .addHeader("Accept", "application/json")
                        .build()
                    chain.proceed(request)
                }.build()

            return Retrofit.Builder()
                .baseUrl("https://pro-api.coinmarketcap.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CoinMarketCapService::class.java)
        }
    }
}