package edu.vt.mobiledev.allmarketstracker.repository

import edu.vt.mobiledev.allmarketstracker.data.PortfolioDao
import edu.vt.mobiledev.allmarketstracker.model.PortfolioTransaction

class PortfolioRepository(private val dao: PortfolioDao) {

    val allTransactions = dao.getAllTransactions()

    suspend fun insert(transaction: PortfolioTransaction) = dao.insertTransaction(transaction)

    suspend fun delete(transaction: PortfolioTransaction) = dao.deleteTransaction(transaction)

    suspend fun clearAll() = dao.clearAll()
}
