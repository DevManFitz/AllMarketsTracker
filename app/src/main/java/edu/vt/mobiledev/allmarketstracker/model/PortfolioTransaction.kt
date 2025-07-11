package edu.vt.mobiledev.allmarketstracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "portfolio_transaction")
data class PortfolioTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coinId: Int?,
    val name: String,
    val symbol: String,
    val logoUrl: String?,
    val amount: Double,
    val purchasePrice: Double,
    val purchaseDate: LocalDate,
    val type: String
)