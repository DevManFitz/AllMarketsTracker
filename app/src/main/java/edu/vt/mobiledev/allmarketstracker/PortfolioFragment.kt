package edu.vt.mobiledev.allmarketstracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.vt.mobiledev.allmarketstracker.databinding.FragmentPortfolioBinding
import edu.vt.mobiledev.allmarketstracker.model.PortfolioTransaction
import edu.vt.mobiledev.allmarketstracker.model.CryptoAsset
import edu.vt.mobiledev.allmarketstracker.AddTransactionDialogFragment
import edu.vt.mobiledev.allmarketstracker.viewmodel.PortfolioViewModel
import edu.vt.mobiledev.allmarketstracker.CryptoListViewModel
import edu.vt.mobiledev.allmarketstracker.model.StockAsset
import edu.vt.mobiledev.allmarketstracker.viewmodel.StockListViewModel
import java.time.LocalDate
import java.text.NumberFormat

class PortfolioFragment : Fragment() {

    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: PortfolioViewModel by viewModels()
    private lateinit var adapter: PortfolioAdapter

    // Use activityViewModels to share with Market tab
    private val assetViewModel: CryptoListViewModel by activityViewModels()
    private val stockListViewModel: StockListViewModel by activityViewModels()
    private var stockAssets: List<StockAsset> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPortfolioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView with LayoutManager
        binding.portfolioRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PortfolioAdapter(emptyList(), assetViewModel.assetsLiveData.value ?: emptyList(), stockAssets) { transaction ->
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Transaction")
                .setMessage("Are you sure you want to delete this transaction?")
                .setPositiveButton("Delete") { _, _ ->
                    viewModel.deleteTransaction(transaction)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
        binding.portfolioRecyclerView.adapter = adapter

        // Observe both transactions and asset list
        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            adapter.updateData(transactions, assetViewModel.assetsLiveData.value ?: emptyList(), stockAssets)
            updatePortfolioSummary(transactions, assetViewModel.assetsLiveData.value, stockAssets)
            val stockSymbolsInPortfolio = transactions.filter { it.type == "stock" }.map { it.symbol }
            stockListViewModel.fetchStocksIfNeeded(stockSymbolsInPortfolio)
        }
        assetViewModel.assetsLiveData.observe(viewLifecycleOwner) { assets ->
            adapter.updateData(viewModel.transactions.value ?: emptyList(), assets, stockAssets)
            updatePortfolioSummary(viewModel.transactions.value ?: emptyList(), assets, stockAssets)
        }
        stockListViewModel.stocksLiveData.observe(viewLifecycleOwner) { stocks ->
            stockAssets = stocks
            adapter.updateData(
                viewModel.transactions.value ?: emptyList(),
                assetViewModel.assetsLiveData.value ?: emptyList(),
                stockAssets
            )
            updatePortfolioSummary(viewModel.transactions.value ?: emptyList(), assetViewModel.assetsLiveData.value, stockAssets)
        }

        binding.addTransactionFab.setOnClickListener {
            TabbedAssetPickerDialogFragment(
                cryptoAssets = assetViewModel.assets.value ?: emptyList(),
                onCryptoPicked = { cryptoAsset ->
                    showAddTransactionDialog(cryptoAsset)
                },
                onStockPicked = { stockAsset ->
                    showAddTransactionDialog(stockAsset)
                }
            ).show(parentFragmentManager, "TabbedAssetPickerDialog")
        }
    }

    private fun showAddTransactionDialog(asset: Any) {
        val dialog = AddTransactionDialogFragment.newInstance(asset)
        dialog.show(parentFragmentManager, "AddTransactionDialog")
    }

    private fun updatePortfolioSummary(
        transactions: List<PortfolioTransaction>?,
        cryptoAssets: List<CryptoAsset>?,
        stockAssets: List<StockAsset>?
    ) {
        if (transactions == null || cryptoAssets == null || stockAssets == null) return

        val costBasis = transactions.sumOf { it.amount * it.purchasePrice }
        val currentValue = transactions.sumOf { tx ->
            val asset = cryptoAssets.find { it.symbol.equals(tx.symbol, ignoreCase = true) }
            tx.amount * (asset?.price ?: 0.0)
        }
        val profit = currentValue - costBasis

        val currencyFormat = NumberFormat.getCurrencyInstance()

        binding.portfolioCostBasis.text = "Cost Basis: ${currencyFormat.format(costBasis)}"

        // Format profit/loss with color and sign
        val profitText = if (profit >= 0) {
            binding.portfolioProfit.setTextColor(requireContext().getColor(android.R.color.holo_green_light))
            "+${currencyFormat.format(profit)}"
        } else {
            binding.portfolioProfit.setTextColor(requireContext().getColor(android.R.color.holo_red_light))
            "-${currencyFormat.format(-profit)}"
        }
        binding.portfolioProfit.text = "Profit/Loss: $profitText"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
