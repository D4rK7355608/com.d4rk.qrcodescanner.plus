package com.d4rk.qrcodescanner.plus.feature.tabs.create.qr
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.d4rk.qrcodescanner.plus.databinding.FragmentCreateQrCodeEmailBinding
import com.d4rk.qrcodescanner.plus.extension.isNotBlank
import com.d4rk.qrcodescanner.plus.extension.textString
import com.d4rk.qrcodescanner.plus.feature.tabs.create.BaseCreateBarcodeFragment
import com.d4rk.qrcodescanner.plus.model.schema.Email
import com.d4rk.qrcodescanner.plus.model.schema.Schema
class CreateQrCodeEmailFragment : BaseCreateBarcodeFragment() {
    private lateinit var _binding: FragmentCreateQrCodeEmailBinding
    private val binding get() = _binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateQrCodeEmailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTitleEditText()
        handleTextChanged()
    }
    override fun getBarcodeSchema(): Schema {
        return Email(email = binding.editTextEmail.textString, subject = binding.editTextSubject.textString, body = binding.editTextMessage.textString)
    }
    private fun initTitleEditText() {
        binding.editTextEmail.requestFocus()
    }
    private fun handleTextChanged() {
        binding.editTextEmail.addTextChangedListener { toggleCreateBarcodeButton() }
        binding.editTextSubject.addTextChangedListener { toggleCreateBarcodeButton() }
        binding.editTextMessage.addTextChangedListener { toggleCreateBarcodeButton() }
    }
    private fun toggleCreateBarcodeButton() {
        parentActivity.isCreateBarcodeButtonEnabled = binding.editTextEmail.isNotBlank() || binding.editTextSubject.isNotBlank() || binding.editTextMessage.isNotBlank()
    }
}