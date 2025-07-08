package edu.vt.mobiledev.allmarketstracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import edu.vt.mobiledev.allmarketstracker.model.CryptoAsset
import edu.vt.mobiledev.allmarketstracker.model.StockAsset

class TabbedAssetPickerDialogFragment(
    private val cryptoAssets: List<CryptoAsset>, // List of crypto assets to display
    private val onCryptoPicked: (CryptoAsset) -> Unit, // Callback for when a crypto asset is picked
    /*  Callback for when a stock asset is picked */ 
    private val onStockPicked: (StockAsset) -> Unit 
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_tabbed_asset_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabLayout = view.findViewById<TabLayout>(R.id.assetTabLayout)

        // Add tabs
        tabLayout.addTab(tabLayout.newTab().setText("Crypto"))
        tabLayout.addTab(tabLayout.newTab().setText("Stock"))

        // Show Crypto picker by default
        showCryptoPicker()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> showCryptoPicker()
                    1 -> showStockPicker()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun showCryptoPicker() {
        val fragment = CoinPickerDialogFragment(cryptoAssets) { asset ->
            onCryptoPicked(asset)
            dismiss()
        }
        swapPickerFragment(fragment)
    }

    private fun showStockPicker() {
        val fragment = StockPickerDialogFragment { stock ->
            onStockPicked(stock)
            dismiss()
        }
        swapPickerFragment(fragment)
    }

    private fun swapPickerFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.assetPickerContainer, fragment)
            .commit()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}