package edu.vt.mobiledev.allmarketstracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import edu.vt.mobiledev.allmarketstracker.databinding.DialogAddTransactionBinding
import java.time.LocalDate
import java.util.*

class AddTransactionDialogFragment(
    private val onTransactionAdded: (amount: Double, price: Double, date: LocalDate) -> Unit
) : DialogFragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.datePickerButton.text = selectedDate.toString()

        binding.datePickerButton.setOnClickListener {
            val now = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    binding.datePickerButton.text = selectedDate.toString()
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.addTransactionButton.setOnClickListener {
            val amount = binding.amountEditText.text.toString().toDoubleOrNull()
            val price = binding.priceEditText.text.toString().toDoubleOrNull()

            if (amount != null && price != null) {
                onTransactionAdded(amount, price, selectedDate)
                dismiss()
            } else {
                binding.errorText.text = "Please enter valid amount and price."
            }
        }

        binding.cancelButton.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}