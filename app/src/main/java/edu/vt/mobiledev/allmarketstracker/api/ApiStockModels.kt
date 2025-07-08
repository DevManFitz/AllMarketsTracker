package edu.vt.mobiledev.allmarketstracker.api

// For /quote endpoint
data class FinnhubQuote(
    val c: Double, // Current price
    val d: Double?, // Change
    val dp: Double?, // Percent change
    val h: Double?, // High price of the day
    val l: Double?, // Low price of the day
    val o: Double?, // Open price of the day
    val pc: Double? // Previous close price
)

// For /stock/profile2 endpoint
data class FinnhubCompanyProfile(
    val name: String?,
    val ticker: String?,
    val logo: String?,
    val exchange: String?,
    val ipo: String?,
    val weburl: String?
)

// For /search endpoint
data class FinnhubSymbolSearchResult(
    val count: Int,
    val result: List<FinnhubSymbol>
)

data class FinnhubSymbol(
    val description: String,
    val displaySymbol: String,
    val symbol: String,
    val type: String
)