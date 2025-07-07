package edu.vt.mobiledev.allmarketstracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import edu.vt.mobiledev.allmarketstracker.databinding.FragmentPortfolioBinding
import edu.vt.mobiledev.allmarketstracker.model.PortfolioTransaction
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

        adapter = PortfolioAdapter(emptyList())
        binding.portfolioRecyclerView.adapter = adapter

        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            adapter.updateData(transactions)
        }

        binding.addTransactionFab.setOnClickListener {
            AddTransactionDialogFragment { amount, price, date ->
                val transaction = PortfolioTransaction(
                    coinId = 1,
                    name = "Bitcoin",
                    symbol = "BTC",
                    amount = amount,
                    purchasePrice = price,
                    purchaseDate = date
                )
                viewModel.addTransaction(transaction)
            }.show(parentFragmentManager, "AddTransactionDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
