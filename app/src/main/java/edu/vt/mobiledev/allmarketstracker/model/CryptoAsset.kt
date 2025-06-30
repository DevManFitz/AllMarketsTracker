package edu.vt.mobiledev.allmarketstracker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CryptoAsset(
    val name: String,
    val symbol: String,
    val price: Double,
    val logoUrl: String
) : Parcelable
