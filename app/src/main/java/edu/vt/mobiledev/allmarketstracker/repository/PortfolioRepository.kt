package edu.vt.mobiledev.allmarketstracker.repository

import edu.vt.mobiledev.allmarketstracker.data.PortfolioDao
import edu.vt.mobiledev.allmarketstracker.model.PortfolioTransaction
import kotlinx.coroutines.flow.Flow

class PortfolioRepository(private val dao: PortfolioDao) {

    val allTransactions: Flow<List<PortfolioTransaction>> = dao.getAllTransactions()

    suspend fun insert(transaction: PortfolioTransaction) = dao.insertTransaction(transaction)

    suspend fun delete(transaction: PortfolioTransaction) = dao.deleteTransaction(transaction)

    suspend fun clear() = dao.clearAll()
}
