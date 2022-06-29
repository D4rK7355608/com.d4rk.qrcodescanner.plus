package com.d4rk.qrcodescanner.plus.feature.tabs.create.qr
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.di.barcodeParser
import com.d4rk.qrcodescanner.plus.extension.isNotBlank
import com.d4rk.qrcodescanner.plus.extension.textString
import com.d4rk.qrcodescanner.plus.feature.tabs.create.BaseCreateBarcodeFragment
import com.d4rk.qrcodescanner.plus.model.schema.Schema
import com.google.zxing.BarcodeFormat
import kotlinx.android.synthetic.main.fragment_create_qr_code_text.edit_text
class CreateQrCodeTextFragment : BaseCreateBarcodeFragment() {
    companion object {
        private const val DEFAULT_TEXT_KEY = "DEFAULT_TEXT_KEY"
        fun newInstance(defaultText: String): CreateQrCodeTextFragment {
            return CreateQrCodeTextFragment().apply {
                arguments = Bundle().apply {
                    putString(DEFAULT_TEXT_KEY, defaultText)
                }
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_qr_code_text, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleTextChanged()
        initEditText()
    }
    override fun getBarcodeSchema(): Schema {
        return barcodeParser.parseSchema(BarcodeFormat.QR_CODE, edit_text.textString)
    }
    private fun initEditText() {
        val defaultText = arguments?.getString(DEFAULT_TEXT_KEY).orEmpty()
        edit_text.apply {
            setText(defaultText)
            setSelection(defaultText.length)
            requestFocus()
        }
    }
    private fun handleTextChanged() {
        edit_text.addTextChangedListener {
            parentActivity.isCreateBarcodeButtonEnabled = edit_text.isNotBlank()
        }
    }
}