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
import com.d4rk.qrcodescanner.plus.databinding.ActivitySaveBarcodeAsTextBinding
import com.d4rk.qrcodescanner.plus.di.barcodeSaver
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

class SaveBarcodeAsTextActivity : BaseActivity() {
    private lateinit var binding : ActivitySaveBarcodeAsTextBinding

    companion object {
        private const val REQUEST_PERMISSIONS_CODE = 101
        private val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        private const val BARCODE_KEY = "BARCODE_KEY"
        fun start(context : Context , barcode : Barcode) {
            val intent = Intent(context , SaveBarcodeAsTextActivity::class.java).apply {
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
        binding = ActivitySaveBarcodeAsTextBinding.inflate(layoutInflater)
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


    private fun supportEdgeToEdge() {
        binding.rootView.applySystemWindowInsets(applyTop = true , applyBottom = true)
    }

    private fun initFormatSpinner() {
        binding.spinnerSaveAs.adapter = ArrayAdapter.createFromResource(
            this , R.array.activity_save_barcode_as_text_formats , R.layout.item_spinner
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
                val saveFunc = when (binding.spinnerSaveAs.selectedItemPosition) {
                    0 -> { ctx: Context, bc: Barcode ->
                        lifecycleScope.launch {
                            barcodeSaver.saveBarcodeAsCsv(ctx, bc)
                        }
                    }
                    1 -> { ctx: Context, bc: Barcode ->
                        lifecycleScope.launch {
                            barcodeSaver.saveBarcodeAsJson(ctx, bc)
                        }
                    }
                    else -> {
                        showLoading(false)
                        return@launch
                    }
                }

                // Offload the saving to IO dispatcher
                withContext(Dispatchers.IO) {
                    saveFunc(this@SaveBarcodeAsTextActivity, barcode)
                }

                // Back on main thread after saving successfully
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
        Snackbar.make(binding.root , R.string.snack_saved_to_downloads , Snackbar.LENGTH_LONG)
                .show()
        finish()
    }
}