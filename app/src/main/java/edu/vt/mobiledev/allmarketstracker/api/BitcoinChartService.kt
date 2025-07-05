package edu.vt.mobiledev.allmarketstracker.api


import retrofit2.http.GET
import retrofit2.http.Query

interface BitcoinChartService {
    @GET("coins/bitcoin/market_chart")
    suspend fun getBitcoinChart(
        @Query("vs_currency") currency: String = "usd",
        @Query("days") days: Int = 1
    ): BitcoinChartResponse
}

data class BitcoinChartResponse(
    val prices: List<List<Double>>
)
