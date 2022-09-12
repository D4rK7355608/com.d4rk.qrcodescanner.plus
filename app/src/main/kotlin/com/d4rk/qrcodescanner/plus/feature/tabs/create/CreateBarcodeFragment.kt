package com.d4rk.qrcodescanner.plus.feature.tabs.create
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.d4rk.qrcodescanner.plus.databinding.FragmentCreateBarcodeBinding
import com.d4rk.qrcodescanner.plus.extension.applySystemWindowInsets
import com.d4rk.qrcodescanner.plus.extension.clipboardManager
import com.d4rk.qrcodescanner.plus.extension.orZero
import com.d4rk.qrcodescanner.plus.feature.tabs.create.barcode.CreateBarcodeAllActivity
import com.d4rk.qrcodescanner.plus.feature.tabs.create.qr.CreateQrCodeAllActivity
import com.d4rk.qrcodescanner.plus.model.schema.BarcodeSchema
import com.google.zxing.BarcodeFormat
import me.zhanghai.android.fastscroll.FastScrollerBuilder
class CreateBarcodeFragment : Fragment() {
    private lateinit var _binding: FragmentCreateBarcodeBinding
    private val binding get() = _binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateBarcodeBinding.inflate(inflater, container, false)
        FastScrollerBuilder(binding.scrollView).useMd2Style().build()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportEdgeToEdge()
        handleButtonsClicked()
    }
    private fun supportEdgeToEdge() {
        binding.appBarLayout.applySystemWindowInsets(applyTop = true)
    }
    private fun handleButtonsClicked() {
        binding.buttonClipboard.setOnClickListener {
            CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.OTHER, getClipboardContent())
        }
        binding.buttonText.setOnClickListener {
            CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.OTHER)
        }
        binding.buttonUrl.setOnClickListener {
            CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.URL)
        }
        binding.buttonWifi.setOnClickListener {
            CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.WIFI)
        }
        binding.buttonLocation.setOnClickListener {
            CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.GEO)
        }
        binding.buttonContactVcard.setOnClickListener {
            CreateBarcodeActivity.start(requireActivity(), BarcodeFormat.QR_CODE, BarcodeSchema.VCARD)
        }
        binding.buttonShowAllQrCode.setOnClickListener {
            CreateQrCodeAllActivity.start(requireActivity())
        }
        binding.buttonCreateBarcode.setOnClickListener {
            CreateBarcodeAllActivity.start(requireActivity())
        }
    }
    private fun getClipboardContent(): String {
        val clip = requireActivity().clipboardManager?.primaryClip ?: return ""
        return when (clip.itemCount.orZero()) {
            0 -> ""
            else -> clip.getItemAt(0).text.toString()
        }
    }
}