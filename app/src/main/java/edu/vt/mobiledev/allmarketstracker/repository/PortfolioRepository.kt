package edu.vt.mobiledev.allmarketstracker.repository

import android.util.Log
import edu.vt.mobiledev.allmarketstracker.data.PortfolioDao
import edu.vt.mobiledev.allmarketstracker.model.PortfolioTransaction
import kotlinx.coroutines.flow.Flow

class PortfolioRepository(private val dao: PortfolioDao) {

    val allTransactions: Flow<List<PortfolioTransaction>> = dao.getAllTransactions()

    suspend fun insert(transaction: PortfolioTransaction) {
        Log.d("PortfolioRepository", "Inserting transaction into database: ${transaction.name}")
        dao.insertTransaction(transaction)
        Log.d("PortfolioRepository", "Transaction inserted successfully")
    }

    suspend fun delete(transaction: PortfolioTransaction) = dao.deleteTransaction(transaction)

    suspend fun clear() = dao.clearAll()
}
