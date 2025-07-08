package edu.vt.mobiledev.allmarketstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import edu.vt.mobiledev.allmarketstracker.api.FinnhubService
import edu.vt.mobiledev.allmarketstracker.model.StockAsset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StockListViewModel : ViewModel() {

    private val finnhubService = FinnhubService.create()
    private val apiKey = "d1m2i89r01qvvurjnh9gd1m2i89r01qvvurjnha0"

    private val _stocks: MutableStateFlow<List<StockAsset>> = MutableStateFlow(emptyList())
    val stocks: StateFlow<List<StockAsset>> = _stocks.asStateFlow()
    val stocksLiveData = stocks.asLiveData()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun searchAndLoadStocks(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val searchResponse = finnhubService.searchSymbol(query, apiKey)
                if (searchResponse.isSuccessful) {
                    val symbols = searchResponse.body()?.result ?: emptyList()
                    val newStockAssets = mutableListOf<StockAsset>()
                    for (symbol in symbols) {
                        // Fetch quote and profile for each symbol
                        val quoteResp = finnhubService.getQuote(symbol.symbol, apiKey)
                        val profileResp = finnhubService.getCompanyProfile(symbol.symbol, apiKey)
                        if (quoteResp.isSuccessful && profileResp.isSuccessful) {
                            val quote = quoteResp.body()
                            val profile = profileResp.body()
                            if (quote != null && profile != null) {
                                newStockAssets.add(
                                    StockAsset(
                                        symbol = symbol.symbol,
                                        name = profile.name ?: symbol.description,
                                        logoUrl = profile.logo,
                                        currentPrice = quote.c,
                                        change = quote.d,
                                        percentChange = quote.dp,
                                        exchange = profile.exchange,
                                        webUrl = profile.weburl
                                    )
                                )
                            }
                        }
                    }
                    // Merge with existing stocks, replacing any with the same symbol
                    val currentStocks = _stocks.value.toMutableList()
                    for (stock in newStockAssets) {
                        val existingIndex = currentStocks.indexOfFirst { it.symbol.equals(stock.symbol, ignoreCase = true) }
                        if (existingIndex >= 0) {
                            currentStocks[existingIndex] = stock
                        } else {
                            currentStocks.add(stock)
                        }
                    }
                    _stocks.value = currentStocks
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchStocksIfNeeded(symbols: List<String>) {
        // For each symbol not already in stocks, fetch and add it
        val missing = symbols.filter { s -> stocks.value.none { it.symbol.equals(s, ignoreCase = true) } }
        for (symbol in missing) {
            searchAndLoadStocks(symbol)
        }
    }
}
