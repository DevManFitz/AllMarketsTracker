package edu.vt.mobiledev.allmarketstracker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.vt.mobiledev.allmarketstracker.model.PortfolioTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: PortfolioTransaction)

    @Query("SELECT * FROM portfolio_transaction ORDER BY purchaseDate DESC")
    fun getAllTransactions(): Flow<List<PortfolioTransaction>>

    @Delete
    suspend fun deleteTransaction(transaction: PortfolioTransaction)

    @Query("DELETE FROM portfolio_transaction")
    suspend fun clearAll()
}