package edu.vt.mobiledev.allmarketstracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TestApiViewModel constructor() : ViewModel() {

    private val coinService = CoinMarketCapService.create()

    fun fetchLatestListings() {
        viewModelScope.launch {
            try {
                val response = coinService.getLatestListings()
                if (response.isSuccessful) {
                    val json = response.body()?.toString()
                    Log.d("API_TEST", "Response:\n$json")
                } else {
                    Log.e("API_TEST", "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("API_TEST", "Exception: ${e.message}")
            }
        }
    }
}