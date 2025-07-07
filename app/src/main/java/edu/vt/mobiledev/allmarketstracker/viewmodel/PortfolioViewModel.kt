package edu.vt.mobiledev.allmarketstracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import edu.vt.mobiledev.allmarketstracker.data.AppDatabase
import edu.vt.mobiledev.allmarketstracker.model.PortfolioTransaction
import edu.vt.mobiledev.allmarketstracker.repository.PortfolioRepository
import kotlinx.coroutines.launch

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PortfolioRepository

    val transactions: LiveData<List<PortfolioTransaction>>

    init {
        val dao = AppDatabase.getInstance(application).portfolioDao()
        repository = PortfolioRepository(dao)
        transactions = repository.allTransactions.asLiveData()
        Log.d("PortfolioViewModel", "ViewModel initialized")
    }

    fun addTransaction(transaction: PortfolioTransaction) = viewModelScope.launch {
        Log.d("PortfolioViewModel", "Adding transaction: ${transaction.name} ${transaction.amount} @ ${transaction.purchasePrice}")
        repository.insert(transaction)
        Log.d("PortfolioViewModel", "Transaction added to repository")
    }

    fun deleteTransaction(transaction: PortfolioTransaction) = viewModelScope.launch {
        repository.delete(transaction)
    }

    fun clearAll() = viewModelScope.launch {
        repository.clear()
    }
}