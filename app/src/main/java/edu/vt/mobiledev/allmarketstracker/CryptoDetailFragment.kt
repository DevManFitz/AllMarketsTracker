package edu.vt.mobiledev.allmarketstracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import edu.vt.mobiledev.allmarketstracker.databinding.FragmentCryptoDetailBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val asset = args.asset

        binding.assetName.text = "${asset.name} (${asset.symbol})"
        binding.assetPrice.text = "$%.4f".format(asset.price)
        binding.assetLogo.load(asset.logoUrl)

        // TODO: Set up line chart view and buttons to toggle timeframes
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}