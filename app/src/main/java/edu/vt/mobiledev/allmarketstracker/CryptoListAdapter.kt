package edu.vt.mobiledev.allmarketstracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.vt.mobiledev.allmarketstracker.databinding.ListItemAssetBinding
import edu.vt.mobiledev.allmarketstracker.model.CryptoAsset

class CryptoListAdapter(
    private val assets: List<CryptoAsset>
) : RecyclerView.Adapter<CryptoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemAssetBinding.inflate(inflater, parent, false)
        return CryptoHolder(binding)
    }

    override fun onBindViewHolder(holder: CryptoHolder, position: Int) {
        val asset = assets[position]
        holder.bind(asset)
    }

    override fun getItemCount(): Int = assets.size
}

class CryptoHolder(
    private val binding: ListItemAssetBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(asset: CryptoAsset) {
        binding.listItemTitle.text = asset.name
        binding.listItemPrice.text = "$${String.format("%.2f", asset.price)}"
        // You can add Glide or Coil here to load asset.imageUrl if available
    }
}

