package edu.vt.mobiledev.allmarketstracker.viewmodel

import androidx.lifecycle.*
import edu.vt.mobiledev.allmarketstracker.model.PortfolioTransaction
import edu.vt.mobiledev.allmarketstracker.repository.PortfolioRepository
import kotlinx.coroutines.launch

class PortfolioViewModel(private val repository: PortfolioRepository) : ViewModel() {

    val transactions = repository.allTransactions

    fun addTransaction(transaction: PortfolioTransaction) {
        viewModelScope.launch {
            repository.insert(transaction)
        }
    }

    fun deleteTransaction(transaction: PortfolioTransaction) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }

    fun clearPortfolio() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}

class PortfolioViewModelFactory(private val repository: PortfolioRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PortfolioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PortfolioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
