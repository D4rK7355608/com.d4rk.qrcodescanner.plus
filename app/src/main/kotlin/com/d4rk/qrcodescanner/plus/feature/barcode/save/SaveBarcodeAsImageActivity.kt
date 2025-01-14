package com.d4rk.qrcodescanner.plus.feature.barcode.save

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.data.model.Barcode
import com.d4rk.qrcodescanner.plus.databinding.ActivitySaveBarcodeAsImageBinding
import com.d4rk.qrcodescanner.plus.di.barcodeImageGenerator
import com.d4rk.qrcodescanner.plus.di.barcodeImageSaver
import com.d4rk.qrcodescanner.plus.di.permissionsHelper
import com.d4rk.qrcodescanner.plus.extension.applySystemWindowInsets
import com.d4rk.qrcodescanner.plus.extension.showError
import com.d4rk.qrcodescanner.plus.extension.unsafeLazy
import com.d4rk.qrcodescanner.plus.feature.BaseActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.zhanghai.android.fastscroll.FastScrollerBuilder

class SaveBarcodeAsImageActivity : BaseActivity() {
    private lateinit var binding : ActivitySaveBarcodeAsImageBinding

    companion object {
        private const val REQUEST_PERMISSIONS_CODE = 101
        private val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        private const val BARCODE_KEY = "BARCODE_KEY"
        fun start(context : Context , barcode : Barcode) {
            val intent = Intent(context , SaveBarcodeAsImageActivity::class.java).apply {
                putExtra(BARCODE_KEY , barcode)
            }
            context.startActivity(intent)
        }
    }

    @Suppress("DEPRECATION")
    private val barcode by unsafeLazy {
        intent?.getSerializableExtra(BARCODE_KEY) as? Barcode
            ?: throw IllegalArgumentException("No barcode passed")
    }
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveBarcodeAsImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportEdgeToEdge()
        initFormatSpinner()
        initSaveButton()
        FastScrollerBuilder(binding.scrollView).useMd2Style().build()
    }

    override fun onRequestPermissionsResult(
        requestCode : Int , permissions : Array<out String> , grantResults : IntArray
    ) {
        super.onRequestPermissionsResult(requestCode , permissions , grantResults)
        if (permissionsHelper.areAllPermissionsGranted(grantResults)) {
            saveBarcode()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun supportEdgeToEdge() {
        binding.rootView.applySystemWindowInsets(applyTop = true , applyBottom = true)
    }

    private fun initFormatSpinner() {
        binding.spinnerSaveAs.adapter = ArrayAdapter.createFromResource(
            this , R.array.activity_save_barcode_as_image_formats , R.layout.item_spinner
        ).apply {
            setDropDownViewResource(R.layout.item_spinner_dropdown)
        }
    }

    private fun initSaveButton() {
        binding.buttonSave.setOnClickListener {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        permissionsHelper.requestPermissions(this , PERMISSIONS , REQUEST_PERMISSIONS_CODE)
    }

    private fun saveBarcode() {
        lifecycleScope.launch {
            showLoading(true)
            try {
                when (binding.spinnerSaveAs.selectedItemPosition) {
                    0 -> {
                        // Generate bitmap and save as PNG
                        val bitmap = withContext(Dispatchers.IO) {
                            barcodeImageGenerator.generateBitmap(barcode, 640, 640, 2)
                        }
                        withContext(Dispatchers.IO) {
                            barcodeImageSaver.savePngImageToPublicDirectory(this@SaveBarcodeAsImageActivity, bitmap, barcode)
                        }
                    }
                    1 -> {
                        // Generate SVG and save
                        val svg = withContext(Dispatchers.IO) {
                            barcodeImageGenerator.generateSvg(barcode, 640, 640, 2)
                        }
                        withContext(Dispatchers.IO) {
                            barcodeImageSaver.saveSvgImageToPublicDirectory(this@SaveBarcodeAsImageActivity, svg, barcode)
                        }
                    }
                    else -> {
                        showLoading(false)
                        return@launch
                    }
                }
                showBarcodeSaved()
            } catch (error: Exception) {
                showLoading(false)
                showError(error)
            }
        }
    }

    private fun showLoading(isLoading : Boolean) {
        binding.progressBarLoading.isVisible = isLoading
        binding.scrollView.isVisible = isLoading.not()
    }

    private fun showBarcodeSaved() {
        Snackbar.make(binding.root , R.string.snack_saved_to_gallery , Snackbar.LENGTH_LONG).show()
        finish()
    }
}