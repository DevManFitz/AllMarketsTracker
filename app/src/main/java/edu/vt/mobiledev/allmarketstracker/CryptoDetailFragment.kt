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

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val asset = args.asset

        binding.assetName.text = "${asset.name} (${asset.symbol})"
        binding.assetPrice.text = "$%.4f".format(asset.price)
        binding.assetLogo.load(asset.logoUrl)

        // TODO: Set up line chart view and buttons to toggle timeframes

        if (asset.symbol.equals("BTC", ignoreCase = true)) {
            lifecycleScope.launch {
                try {
                    val response = BitcoinChartApi.retrofitService.getBitcoinChart(days = 1)
                    val prices = response.prices.mapIndexed { index, pair ->
                        Entry(index.toFloat(), pair[1].toFloat())
                    }

                    val dataSet = LineDataSet(prices, "BTC Price")
                    dataSet.color = Color.CYAN
                    dataSet.valueTextColor = Color.WHITE
                    dataSet.setDrawFilled(true)

                    val lineData = LineData(dataSet)
                    binding.lineChart.data = lineData
                    binding.lineChart.description.text = "24h Price"
                    binding.lineChart.setTouchEnabled(true)
                    binding.lineChart.setPinchZoom(true)
                    binding.lineChart.invalidate()
                } catch (e: HttpException) {
                    binding.lineChart.setNoDataText("HTTP error: ${e.code()}")
                } catch (e: Exception) {
                    val errorCode = if (e is HttpException) e.code() else "unknown"
                    binding.lineChart.setNoDataText("Chart load failed (HTTP $errorCode)")
                }
            }
        } else {
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
        _binding = null
    }
}