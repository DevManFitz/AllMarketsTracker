package edu.vt.mobiledev.allmarketstracker

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.vt.mobiledev.allmarketstracker.model.CryptoAsset

class CoinPickerDialogFragment(
    private val assets: List<CryptoAsset>,
    private val onCoinPicked: (CryptoAsset) -> Unit
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_coin_picker, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.coinPickerRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CoinPickerAdapter(assets) { asset ->
            onCoinPicked(asset)
            dismiss()
        }
        return view
    }
}