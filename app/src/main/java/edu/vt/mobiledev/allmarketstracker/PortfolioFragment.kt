package edu.vt.mobiledev.allmarketstracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.vt.mobiledev.allmarketstracker.databinding.FragmentPortfolioBinding
import edu.vt.mobiledev.allmarketstracker.model.PortfolioTransaction
import edu.vt.mobiledev.allmarketstracker.model.CryptoAsset
import edu.vt.mobiledev.allmarketstracker.AddTransactionDialogFragment
import edu.vt.mobiledev.allmarketstracker.viewmodel.PortfolioViewModel
import java.time.LocalDate

class PortfolioFragment : Fragment() {

    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: PortfolioViewModel by viewModels()
    private lateinit var adapter: PortfolioAdapter

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
            // For now, we'll create a default Bitcoin asset
            // TODO: In the future, open a coin picker dialog
            val defaultAsset = CryptoAsset(
                id = 1,
                name = "Bitcoin",
                symbol = "BTC",
                price = 0.0,
                logoUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/1.png"
            )
            
            AddTransactionDialogFragment(defaultAsset) { amount, price, date ->
                Log.d("PortfolioFragment", "Creating transaction: amount=$amount, price=$price, date=$date")
                val transaction = PortfolioTransaction(
                    coinId = defaultAsset.id,
                    name = defaultAsset.name,
                    symbol = defaultAsset.symbol,
                    logoUrl = defaultAsset.logoUrl,
                    amount = amount,
                    purchasePrice = price,
                    purchaseDate = date
                )
                Log.d("PortfolioFragment", "Adding transaction to ViewModel")
                viewModel.addTransaction(transaction)
            }.show(parentFragmentManager, "AddTransactionDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
