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

data class CryptoInfoResponse(
    val data: Map<String, CryptoInfo>
)

data class CryptoInfo(
    val logo: String
)

data class QuoteResponse(
    val data: Map<String, CoinQuote>
)

data class CoinQuote(
    val id: Int,
    val name: String,
    val symbol: String,
    val cmc_rank: Int?,
    val circulating_supply: Double?,
    val total_supply: Double?,
    val max_supply: Double?,
    val quote: Map<String, QuoteDetail>
)

data class QuoteDetail(
    val price: Double,
    val volume_24h: Double,
    val market_cap: Double,
    val fully_diluted_market_cap: Double
)
