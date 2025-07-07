package edu.vt.mobiledev.allmarketstracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.vt.mobiledev.allmarketstracker.model.PortfolioTransaction

class PortfolioAdapter(
    private var transactions: List<PortfolioTransaction>
) : RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder>() {

    class PortfolioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val coinName: TextView = view.findViewById(R.id.coin_name)
        private val amountText: TextView = view.findViewById(R.id.amount_text)
        private val priceText: TextView = view.findViewById(R.id.price_text)
        private val dateText: TextView = view.findViewById(R.id.date_text)

        fun bind(transaction: PortfolioTransaction) {
            coinName.text = "${transaction.name} (${transaction.symbol})"
            amountText.text = "Amount: ${transaction.amount}"
            priceText.text = "Purchase Price: $${transaction.purchasePrice}"
            dateText.text = "Date: ${transaction.purchaseDate}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_portfolio_transaction, parent, false)
        return PortfolioViewHolder(view)
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size

    fun updateData(newTransactions: List<PortfolioTransaction>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}
