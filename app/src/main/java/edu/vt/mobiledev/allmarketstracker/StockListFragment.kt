package edu.vt.mobiledev.allmarketstracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import edu.vt.mobiledev.allmarketstracker.databinding.FragmentStockListBinding
import edu.vt.mobiledev.allmarketstracker.viewmodel.StockListViewModel

class StockListFragment : Fragment() {

    private var _binding: FragmentStockListBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: StockListViewModel by viewModels()
    private lateinit var adapter: StockListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStockListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = StockListAdapter(emptyList())
        binding.stockRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.stockRecyclerView.adapter = adapter

        // Example: load some stocks on start (or use a search bar)
        viewModel.searchAndLoadStocks("DB")
        viewModel.searchAndLoadStocks("HD")
        viewModel.searchAndLoadStocks("AAPL")
        viewModel.searchAndLoadStocks("GOOGL")
        viewModel.searchAndLoadStocks("TSLA")
        viewModel.searchAndLoadStocks("JPM")
        viewModel.searchAndLoadStocks("BLK")


        viewModel.stocksLiveData.observe(viewLifecycleOwner) { stocks ->
            adapter.updateData(stocks)
        }
        // listen for queries and trigger the ViewModel search.
        val searchView = binding.stockSearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchAndLoadStocks(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optionally, search as user types
                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
