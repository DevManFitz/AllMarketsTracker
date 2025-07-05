package edu.vt.mobiledev.allmarketstracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import edu.vt.mobiledev.allmarketstracker.databinding.FragmentCryptoDetailBinding

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import edu.vt.mobiledev.allmarketstracker.api.BitcoinChartApi
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.HttpException


class CryptoDetailFragment : Fragment() {

    private var _binding: FragmentCryptoDetailBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is null" }

    private val args: CryptoDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCryptoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun temporarilyDisableButtons() {
        val buttons = listOf(
            binding.btn1h,
            binding.btn24h,
            binding.btn7d,
            binding.btn30d,
            binding.btn1y,
            binding.btnAll
        )
        buttons.forEach { it.isEnabled = false }

        binding.root.postDelayed({
            buttons.forEach { it.isEnabled = true }
        }, 2000L) // re-enable after 2 seconds
    }

    private fun loadBitcoinChart(days: Int) {
        temporarilyDisableButtons()
        binding.lineChart.setNoDataText("Loading chart...")

        binding.lineChart.clear()
        binding.lineChart.data = null
        binding.lineChart.invalidate()

        lifecycleScope.launch {
            try {
                val response = BitcoinChartApi.retrofitService.getBitcoinChart(days = days)
                val prices = response.prices.mapIndexed { index, pair ->
                    Entry(index.toFloat(), pair[1].toFloat())
                }

                val dataSet = LineDataSet(prices, "BTC Price")
                dataSet.color = Color.CYAN
                dataSet.valueTextColor = Color.WHITE
                dataSet.setDrawFilled(true)

                val lineData = LineData(dataSet)

                with(binding.lineChart) {
                    clear() // Force reset old data
                    data = lineData
                    description.text = when (days) {
                        1 -> "24h Price"
                        7 -> "7d Price"
                        30 -> "30d Price"
                        365 -> "1y Price"
                        1095 -> "3y Price"
                        else -> "$days-day Price"
                    }
                    setTouchEnabled(true)
                    setPinchZoom(true)
                    notifyDataSetChanged() // Force re-render
                    invalidate() // Refresh chart
                    Log.d("ChartDebug", "Entry count: ${binding.lineChart.data?.entryCount}")
                }
            } catch (e: HttpException) {
                val errorCode = e.code()
                val message = when (errorCode) {
                    429 -> {
                        Log.d("ChartError", "Rate limit exceeded (429). Showing fallback message.")
                        "Rate limit exceeded. Please wait and try again."
                    }
                    else -> {
                        Log.d("ChartError", "HTTP error: $errorCode")
                        "HTTP error: $errorCode"
                    }
                }

                with(binding.lineChart) {
                    clear()
                    data = null
                    setNoDataText(message)
                    invalidate()
                }
            } catch (e: Exception) {
                Log.e("ChartError", "Unexpected error", e)
                with(binding.lineChart) {
                    clear()
                    data = null
                    setNoDataText("Chart load failed.")
                    invalidate()
                }
            }
        }
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val asset = args.asset

        binding.assetName.text = "${asset.name} (${asset.symbol})"
        binding.assetPrice.text = "$%.4f".format(asset.price)
        binding.assetLogo.load(asset.logoUrl)

        // TODO: Set up line chart view and buttons to toggle timeframes

        if (asset.symbol.equals("BTC", ignoreCase = true)) {
            loadBitcoinChart(days = 1)

            binding.btn1h.setOnClickListener { loadBitcoinChart(days = 1) }
            binding.btn24h.setOnClickListener { loadBitcoinChart(days = 1) }
            binding.btn7d.setOnClickListener { loadBitcoinChart(days = 7) }
            binding.btn30d.setOnClickListener { loadBitcoinChart(days = 30) }
            binding.btn1y.setOnClickListener { loadBitcoinChart(days = 365) }
            binding.btnAll.setOnClickListener { loadBitcoinChart(days = 1095) }
        }
        else {
            val entries = listOf(
                Entry(0f, 10f),
                Entry(1f, 12f),
                Entry(2f, 9f),
                Entry(3f, 14f),
                Entry(4f, 13f)
            )
            val dataSet = LineDataSet(entries, "${asset.symbol} Price")
            dataSet.color = Color.GRAY
            dataSet.valueTextColor = Color.WHITE
            dataSet.setDrawFilled(true)
            binding.lineChart.data = LineData(dataSet)
            binding.lineChart.invalidate()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.lineChart.clear()
        _binding = null
    }
}