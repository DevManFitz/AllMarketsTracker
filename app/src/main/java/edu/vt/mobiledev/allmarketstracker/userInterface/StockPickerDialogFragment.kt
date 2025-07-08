package edu.vt.mobiledev.allmarketstracker.userInterface

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.vt.mobiledev.allmarketstracker.R
import edu.vt.mobiledev.allmarketstracker.model.StockAsset
import edu.vt.mobiledev.allmarketstracker.viewmodel.StockListViewModel

class StockPickerDialogFragment(
    private val onStockPicked: (StockAsset) -> Unit
) : DialogFragment() {

    private val viewModel: StockListViewModel by viewModels()
    private lateinit var adapter: StockListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_stock_picker, container, false)
        val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.stockPickerRecyclerView)
        val searchView = view.findViewById<SearchView>(R.id.stockSearchView)
        val loadingBar = view.findViewById<ProgressBar>(R.id.stockLoadingBar)

        adapter = StockListAdapter(emptyList()) { stock ->
            onStockPicked(stock)
            dismiss()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.stocksLiveData.observe(viewLifecycleOwner) { stocks ->
            adapter.updateData(stocks)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchAndLoadStocks(it) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                // Optionally, search as user types
                return false
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect { loading ->
                loadingBar.visibility = if (loading) View.VISIBLE else View.GONE
            }
        }

        return view
    }
}
