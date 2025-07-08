package edu.vt.mobiledev.allmarketstracker.userInterface

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import edu.vt.mobiledev.allmarketstracker.R
import edu.vt.mobiledev.allmarketstracker.model.CryptoAsset
import edu.vt.mobiledev.allmarketstracker.model.PortfolioTransaction
import edu.vt.mobiledev.allmarketstracker.model.StockAsset
import java.math.RoundingMode
import java.text.NumberFormat

class PortfolioAdapter(
    private var transactions: List<PortfolioTransaction>,
    private var cryptoAssets: List<CryptoAsset>,
    private var stockAssets: List<StockAsset>,
    private val onLongClick: (PortfolioTransaction) -> Unit
) : RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder>() {

    class PortfolioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val coinLogo: ImageView = view.findViewById(R.id.asset_logo)
        private val coinName: TextView = view.findViewById(R.id.coin_name)
        private val amountText: TextView = view.findViewById(R.id.amount_text)
        private val priceText: TextView = view.findViewById(R.id.price_text)
        private val dateText: TextView = view.findViewById(R.id.date_text)
        private val profitLossText: TextView = view.findViewById(R.id.profit_loss_text)
        private val costBasisText: TextView = view.findViewById(R.id.cost_basis_text)

        fun bind(
            transaction: PortfolioTransaction,
            cryptoAssets: List<CryptoAsset>,
            stockAssets: List<StockAsset>,
            onLongClick: (PortfolioTransaction) -> Unit
        ) {
            val currencyFormat = NumberFormat.getCurrencyInstance()

            // Load the coin logo using Coil
            coinLogo.load(transaction.logoUrl) {
                crossfade(true)
                placeholder(R.drawable.bch_logo) // fallback image
                error(R.drawable.error)
            }
            
            coinName.text = "${transaction.name} (${transaction.symbol})"
            // Format amount with commas and up to 8 decimals
            val amountFormatted = NumberFormat.getNumberInstance().apply {
                minimumFractionDigits = 0
                maximumFractionDigits = 8
                roundingMode = RoundingMode.DOWN
            }.format(transaction.amount)
            amountText.text = "Amount: $amountFormatted"
            priceText.text = "Purchase Price: $${transaction.purchasePrice}"
            dateText.text = "Date: ${transaction.purchaseDate}"

            // Calculate profit/loss for this transaction
            val currentPrice = when (transaction.type) {
                "crypto" -> cryptoAssets.find { it.symbol.equals(transaction.symbol, ignoreCase = true) }?.price ?: 0.0
                "stock" -> stockAssets.find { it.symbol.equals(transaction.symbol, ignoreCase = true) }?.currentPrice ?: Double.NaN
                else -> Double.NaN
            }
            val profit = if (currentPrice.isNaN()) null else (currentPrice - transaction.purchasePrice) * transaction.amount

            if (profit == null) {
                profitLossText.text = "Profit/Loss: N/A"
                profitLossText.setTextColor(itemView.context.getColor(android.R.color.darker_gray))
            } else {
                val profitText = if (profit >= 0) {
                    profitLossText.setTextColor(itemView.context.getColor(android.R.color.holo_green_light))
                    "+${currencyFormat.format(profit)}"
                } else {
                    profitLossText.setTextColor(itemView.context.getColor(android.R.color.holo_red_light))
                    "-${currencyFormat.format(-profit)}"
                }
                profitLossText.text = "Profit/Loss: $profitText"
            }

            // Cost basis
            val costBasis = transaction.amount * transaction.purchasePrice
            costBasisText.text = "Cost Basis: ${currencyFormat.format(costBasis)}"

            itemView.setOnLongClickListener {
                onLongClick(transaction)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_portfolio_transaction, parent, false)
        return PortfolioViewHolder(view)
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        holder.bind(transactions[position], cryptoAssets, stockAssets, onLongClick)
    }

    override fun getItemCount(): Int = transactions.size

    fun updateData(
        newTransactions: List<PortfolioTransaction>,
        newCryptoAssets: List<CryptoAsset>,
        newStockAssets: List<StockAsset>
    ) {
        transactions = newTransactions
        cryptoAssets = newCryptoAssets
        stockAssets = newStockAssets
        notifyDataSetChanged()
    }
}
