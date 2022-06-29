package com.d4rk.qrcodescanner.plus.feature.tabs.create.qr
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.extension.isNotBlank
import com.d4rk.qrcodescanner.plus.extension.textString
import com.d4rk.qrcodescanner.plus.feature.tabs.create.BaseCreateBarcodeFragment
import com.d4rk.qrcodescanner.plus.model.schema.Cryptocurrency
import com.d4rk.qrcodescanner.plus.model.schema.Schema
import kotlinx.android.synthetic.main.fragment_create_qr_code_cryptocurrency.spinner_cryptocurrency
import kotlinx.android.synthetic.main.fragment_create_qr_code_cryptocurrency.edit_text_address
import kotlinx.android.synthetic.main.fragment_create_qr_code_cryptocurrency.edit_text_amount
import kotlinx.android.synthetic.main.fragment_create_qr_code_cryptocurrency.edit_text_label
import kotlinx.android.synthetic.main.fragment_create_qr_code_cryptocurrency.edit_text_message
class CreateQrCodeCryptocurrencyFragment : BaseCreateBarcodeFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_qr_code_cryptocurrency, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCryptocurrenciesSpinner()
        initAddressEditText()
        handleTextChanged()
    }
    override fun getBarcodeSchema(): Schema {
        val cryptocurrency = when (spinner_cryptocurrency.selectedItemPosition) {
            0 -> "bitcoin"
            1 -> "bitcoincash"
            2 -> "ethereum"
            3 -> "litecoin"
            4 -> "dash"
            else -> "bitcoin"
        }
        return Cryptocurrency(
            cryptocurrency = cryptocurrency,
            address = edit_text_address.textString,
            label = edit_text_label.textString,
            amount = edit_text_amount.textString,
            message = edit_text_message.textString
        )
    }
    private fun initCryptocurrenciesSpinner() {
        spinner_cryptocurrency.adapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.fragment_create_qr_code_cryptocurrencies, R.layout.item_spinner
        ).apply {
            setDropDownViewResource(R.layout.item_spinner_dropdown)
        }
    }
    private fun initAddressEditText() {
        edit_text_address.requestFocus()
    }
    private fun handleTextChanged() {
        listOf(edit_text_address, edit_text_amount, edit_text_label, edit_text_message).forEach { editText ->
            editText.addTextChangedListener { toggleCreateBarcodeButton() }
        }
    }
    private fun toggleCreateBarcodeButton() {
        parentActivity.isCreateBarcodeButtonEnabled = edit_text_address.isNotBlank()
    }
}