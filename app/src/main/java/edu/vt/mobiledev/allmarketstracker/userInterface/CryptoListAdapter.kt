package edu.vt.mobiledev.allmarketstracker.userInterface

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import edu.vt.mobiledev.allmarketstracker.R
import edu.vt.mobiledev.allmarketstracker.databinding.ListItemAssetBinding
import edu.vt.mobiledev.allmarketstracker.model.CryptoAsset

class CryptoListAdapter(
    private val assets: List<CryptoAsset>,
    private val onItemClick: (CryptoAsset) -> Unit
) : RecyclerView.Adapter<CryptoListAdapter.CryptoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemAssetBinding.inflate(inflater, parent, false)
        return CryptoHolder(binding)
    }

    override fun onBindViewHolder(holder: CryptoHolder, position: Int) {
        val asset = assets[position]
        holder.bind(asset)

        holder.itemView.setOnClickListener {
            onItemClick(asset)
        }
    }

    override fun getItemCount(): Int = assets.size

    class CryptoHolder(
        private val binding: ListItemAssetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(asset: CryptoAsset) {
            binding.assetName.text = "${asset.name} (${asset.symbol})"
            binding.assetPrice.text = "$%.4f".format(asset.price)
            binding.assetLogo.load(asset.logoUrl) {
                crossfade(true)
                placeholder(R.drawable.bch_logo)
                error(R.drawable.error)
            }
        }
    }
}