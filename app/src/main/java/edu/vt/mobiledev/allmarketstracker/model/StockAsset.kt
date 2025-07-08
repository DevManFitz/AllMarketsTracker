package edu.vt.mobiledev.allmarketstracker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockAsset(
    val symbol: String,
    val name: String,
    val logoUrl: String?,
    val currentPrice: Double,
    val change: Double?,         // Price change
    val percentChange: Double?,  // Percent change
    val exchange: String?,
    val webUrl: String?
) : Parcelable
