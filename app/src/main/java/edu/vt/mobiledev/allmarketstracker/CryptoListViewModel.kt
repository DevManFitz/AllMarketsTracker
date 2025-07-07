package edu.vt.mobiledev.allmarketstracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import edu.vt.mobiledev.allmarketstracker.api.ApiCoin
import edu.vt.mobiledev.allmarketstracker.model.CryptoAsset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CryptoListViewModel : ViewModel() {

    private val coinService = CoinMarketCapService.create()

    private val _assets: MutableStateFlow<List<CryptoAsset>> = MutableStateFlow(emptyList())
    val assets: StateFlow<List<CryptoAsset>> = _assets.asStateFlow()

    val assetsLiveData = assets.asLiveData()

    init {
        fetchAssets()
    }

    private fun fetchAssets() {
        viewModelScope.launch {
            try {
                val response = coinService.getLatestListings()
                if (response.isSuccessful) {
                    val apiCoins = response.body()?.data ?: emptyList()

                    val ids = apiCoins.joinToString(",") { it.id.toString() }
                    val infoResponse = coinService.getCryptoInfo(ids)
                    val logos = infoResponse.body()?.data ?: emptyMap()

                    _assets.value = apiCoins.map { apiCoin ->
                        val logoUrl = logos[apiCoin.id.toString()]?.logo.orEmpty()
                        Log.d("LOGO_URL", "${apiCoin.name}: $logoUrl")
                        apiCoin.toCryptoAsset(logoUrl)
                    }
                }
            } catch (e: Exception) {
                // Log or handle error
            }
        }
    }

    private fun ApiCoin.toCryptoAsset(logoUrl: String): CryptoAsset {
        return CryptoAsset(
            id = id,
            name = name,
            symbol = symbol,
            price = quote["USD"]?.price ?: 0.0,
            logoUrl = logoUrl
        )
    }
}