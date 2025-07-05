package edu.vt.mobiledev.allmarketstracker.api

object BitcoinChartApi {
    private const val BASE_URL = "https://api.coingecko.com/api/v3/"

    val retrofitService: BitcoinChartService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(BitcoinChartService::class.java)
    }
}