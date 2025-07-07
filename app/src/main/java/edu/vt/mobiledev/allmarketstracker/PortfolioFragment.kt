package edu.vt.mobiledev.allmarketstracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import java.time.LocalDate

class PortfolioFragment : Fragment() {

    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: PortfolioViewModel by viewModels()
    private lateinit var adapter: PortfolioAdapter

    // Use activityViewModels to share with Market tab
    private val assetViewModel: CryptoListViewModel by activityViewModels()

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
        adapter = PortfolioAdapter(emptyList())
        binding.portfolioRecyclerView.adapter = adapter

        // Observe transactions and log changes
        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            Log.d("PortfolioFragment", "Received ${transactions.size} transactions")
            adapter.updateData(transactions)
        }

        binding.addTransactionFab.setOnClickListener {
            // Get the latest asset list from the shared ViewModel
            assetViewModel.assets.value.let { assetList ->
                if (assetList.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "No coins available", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                CoinPickerDialogFragment(assetList) { selectedAsset ->
                    AddTransactionDialogFragment(selectedAsset) { amount, price, date ->
                        val transaction = PortfolioTransaction(
                            coinId = selectedAsset.id,
                            name = selectedAsset.name,
                            symbol = selectedAsset.symbol,
                            logoUrl = selectedAsset.logoUrl,
                            amount = amount,
                            purchasePrice = price,
                            purchaseDate = date
                        )
                        viewModel.addTransaction(transaction)
                    }.show(parentFragmentManager, "AddTransactionDialog")
                }.show(parentFragmentManager, "CoinPickerDialog")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
