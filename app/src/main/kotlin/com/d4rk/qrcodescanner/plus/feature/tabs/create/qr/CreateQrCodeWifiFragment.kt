package com.d4rk.qrcodescanner.plus.feature.tabs.create.qr
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.extension.isNotBlank
import com.d4rk.qrcodescanner.plus.extension.textString
import com.d4rk.qrcodescanner.plus.feature.tabs.create.BaseCreateBarcodeFragment
import com.d4rk.qrcodescanner.plus.model.schema.Schema
import com.d4rk.qrcodescanner.plus.model.schema.Wifi
import kotlinx.android.synthetic.main.fragment_create_qr_code_wifi.edit_text_network_name
import kotlinx.android.synthetic.main.fragment_create_qr_code_wifi.edit_text_password
import kotlinx.android.synthetic.main.fragment_create_qr_code_wifi.check_box_is_hidden
import kotlinx.android.synthetic.main.fragment_create_qr_code_wifi.spinner_encryption
import kotlinx.android.synthetic.main.fragment_create_qr_code_wifi.text_input_layout_password
class CreateQrCodeWifiFragment : BaseCreateBarcodeFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_qr_code_wifi, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEncryptionTypesSpinner()
        initNetworkNameEditText()
        handleTextChanged()
    }
    override fun getBarcodeSchema(): Schema {
        val encryption = when (spinner_encryption.selectedItemPosition) {
            0 -> "WPA"
            1 -> "WEP"
            2 -> "nopass"
            else -> "nopass"
        }
        return Wifi(
            encryption = encryption,
            name = edit_text_network_name.textString,
            password = edit_text_password.textString,
            isHidden = check_box_is_hidden.isChecked
        )
    }
    private fun initEncryptionTypesSpinner() {
        spinner_encryption.adapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.fragment_create_qr_code_wifi_encryption_types, R.layout.item_spinner
        ).apply {
            setDropDownViewResource(R.layout.item_spinner_dropdown)
        }
        spinner_encryption.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                text_input_layout_password.isVisible = position != 2
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    private fun initNetworkNameEditText() {
        edit_text_network_name.requestFocus()
    }
    private fun handleTextChanged() {
        edit_text_network_name.addTextChangedListener { toggleCreateBarcodeButton() }
        edit_text_password.addTextChangedListener { toggleCreateBarcodeButton() }
    }
    private fun toggleCreateBarcodeButton() {
        parentActivity.isCreateBarcodeButtonEnabled = edit_text_network_name.isNotBlank()
    }
}