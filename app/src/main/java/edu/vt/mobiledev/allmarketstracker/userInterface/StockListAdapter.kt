package edu.vt.mobiledev.allmarketstracker.userInterface

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import edu.vt.mobiledev.allmarketstracker.R
import edu.vt.mobiledev.allmarketstracker.model.StockAsset
import java.text.NumberFormat

class StockListAdapter(
    private var stocks: List<StockAsset>,
    private val onItemClick: ((StockAsset) -> Unit)? = null
) : RecyclerView.Adapter<StockListAdapter.StockViewHolder>() {

    class StockViewHolder(
        view: View,
        private val onItemClick: ((StockAsset) -> Unit)?
    ) : RecyclerView.ViewHolder(view) {
        private val logo: ImageView = view.findViewById(R.id.stock_logo)
        private val name: TextView = view.findViewById(R.id.stock_name)
        private val symbol: TextView = view.findViewById(R.id.stock_symbol)
        private val exchange: TextView = view.findViewById(R.id.stock_exchange)
        private val price: TextView = view.findViewById(R.id.stock_price)
        private val change: TextView = view.findViewById(R.id.stock_change)

        fun bind(stock: StockAsset) {
            logo.load(stock.logoUrl) {
                crossfade(true)
                placeholder(R.drawable.bch_logo)
                error(R.drawable.error)
            }
            name.text = stock.name
            symbol.text = stock.symbol
            exchange.text = stock.exchange ?: ""
            price.text = NumberFormat.getCurrencyInstance().format(stock.currentPrice)

            // Format change and color
            val changeValue = stock.change ?: 0.0
            val percent = stock.percentChange ?: 0.0
            val changeText = if (changeValue >= 0) {
                change.setTextColor(itemView.context.getColor(android.R.color.holo_green_light))
                "+${NumberFormat.getCurrencyInstance().format(changeValue)} (${String.format("%.2f", percent)}%)"
            } else {
                change.setTextColor(itemView.context.getColor(android.R.color.holo_red_light))
                "-${NumberFormat.getCurrencyInstance().format(-changeValue)} (${String.format("%.2f", percent)}%)"
            }
            change.text = changeText

            itemView.setOnClickListener {
                onItemClick?.invoke(stock)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stock, parent, false)
        return StockViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(stocks[position])
    }

    override fun getItemCount(): Int = stocks.size

    fun updateData(newStocks: List<StockAsset>) {
        stocks = newStocks
        notifyDataSetChanged()
    }
}
