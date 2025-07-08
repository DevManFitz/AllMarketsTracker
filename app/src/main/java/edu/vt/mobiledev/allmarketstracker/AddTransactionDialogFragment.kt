package edu.vt.mobiledev.allmarketstracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import coil.load
import edu.vt.mobiledev.allmarketstracker.databinding.DialogAddTransactionBinding
import edu.vt.mobiledev.allmarketstracker.model.CryptoAsset
import edu.vt.mobiledev.allmarketstracker.model.PortfolioTransaction
import edu.vt.mobiledev.allmarketstracker.model.StockAsset
import edu.vt.mobiledev.allmarketstracker.viewmodel.PortfolioViewModel
import java.time.LocalDate
import java.util.*

class AddTransactionDialogFragment : DialogFragment() {

    private val portfolioViewModel: PortfolioViewModel by activityViewModels()

    companion object {
        private const val ARG_CRYPTO = "arg_crypto"
        private const val ARG_STOCK = "arg_stock"

        fun newInstance(asset: Any): AddTransactionDialogFragment {
            val fragment = AddTransactionDialogFragment()
            val args = Bundle()
            when (asset) {
                is CryptoAsset -> args.putParcelable(ARG_CRYPTO, asset)
                is StockAsset -> args.putParcelable(ARG_STOCK, asset)
                else -> throw IllegalArgumentException("Unsupported asset type")
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: DialogAddTransactionBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Set dialog width to 90% of screen width
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cryptoAsset = arguments?.getParcelable<CryptoAsset>(ARG_CRYPTO)
        val stockAsset = arguments?.getParcelable<StockAsset>(ARG_STOCK)
        // Use whichever is not null to pre-fill your dialog
        
        // Display the selected coin information
        binding.coinName.text = "${cryptoAsset?.name} (${cryptoAsset?.symbol})"
        binding.coinLogo.load(cryptoAsset?.logoUrl) {
            crossfade(true)
            placeholder(R.drawable.bch_logo)
            error(R.drawable.error)
        }
        
        binding.datePickerButton.text = selectedDate.toString()

        binding.datePickerButton.setOnClickListener {
            val now = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    binding.datePickerButton.text = selectedDate.toString()
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.calendarViewShown = false
            datePickerDialog.datePicker.spinnersShown = true
            datePickerDialog.show()
        }

        binding.addTransactionButton.setOnClickListener {
            val amount = binding.amountEditText.text.toString().toDoubleOrNull()
            val price = binding.priceEditText.text.toString().toDoubleOrNull()

            if (cryptoAsset != null && amount != null && price != null) {
                val transaction = PortfolioTransaction(
                    coinId = cryptoAsset.id,
                    name = cryptoAsset.name,
                    symbol = cryptoAsset.symbol,
                    logoUrl = cryptoAsset.logoUrl,
                    amount = amount,
                    purchasePrice = price,
                    purchaseDate = selectedDate
                )
                portfolioViewModel.addTransaction(transaction)
                dismiss()
            } else {
                binding.errorText.text = "Please enter valid amount and price."
                binding.errorText.visibility = View.VISIBLE
            }
        }

        binding.cancelButton.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}