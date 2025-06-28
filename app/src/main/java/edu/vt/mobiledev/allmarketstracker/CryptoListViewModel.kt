package edu.vt.mobiledev.allmarketstracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        fetchAssets()
    }

    private fun fetchAssets() {
        viewModelScope.launch {
            try {
                val response = coinService.getLatestListings()
                if (response.isSuccessful) {
                    val apiCoins = response.body()?.data ?: emptyList()
                    _assets.value = apiCoins.map { it.toCryptoAsset() }
                }
            } catch (e: Exception) {
                // Log or handle error
            }
        }
    }

    private fun ApiCoin.toCryptoAsset(): CryptoAsset {
        return CryptoAsset(
            name = name,
            symbol = symbol,
            price = quote["USD"]?.price ?: 0.0
        )
    }
}