package edu.vt.mobiledev.allmarketstracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import edu.vt.mobiledev.allmarketstracker.model.CryptoAsset

class CoinPickerAdapter(
    private val assets: List<CryptoAsset>,
    private val onItemClick: (CryptoAsset) -> Unit
) : RecyclerView.Adapter<CoinPickerAdapter.CoinViewHolder>() {

    class CoinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val logo: ImageView = view.findViewById(R.id.coin_logo)
        private val name: TextView = view.findViewById(R.id.coin_name)
        private val symbol: TextView = view.findViewById(R.id.coin_symbol)

        fun bind(asset: CryptoAsset, onClick: (CryptoAsset) -> Unit) {
            logo.load(asset.logoUrl) {
                crossfade(true)
                placeholder(R.drawable.bch_logo)
                error(R.drawable.error)
            }
            name.text = asset.name
            symbol.text = asset.symbol
            itemView.setOnClickListener { onClick(asset) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coin_picker, parent, false)
        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(assets[position], onItemClick)
    }

    override fun getItemCount(): Int = assets.size
}