package edu.vt.mobiledev.allmarketstracker.api

data class CoinMarketCapResponse(
    val data: List<ApiCoin>
)

data class ApiCoin(
    val id: Int,
    val name: String,
    val symbol: String,
    val quote: Map<String, Quote>
)

data class Quote(
    val price: Double
)
