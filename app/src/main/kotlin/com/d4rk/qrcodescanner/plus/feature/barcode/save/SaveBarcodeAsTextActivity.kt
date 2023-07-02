package com.d4rk.qrcodescanner.plus.feature.barcode.save
import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.ActivitySaveBarcodeAsTextBinding
import com.d4rk.qrcodescanner.plus.di.barcodeSaver
import com.d4rk.qrcodescanner.plus.di.permissionsHelper
import com.d4rk.qrcodescanner.plus.extension.applySystemWindowInsets
import com.d4rk.qrcodescanner.plus.extension.showError
import com.d4rk.qrcodescanner.plus.extension.unsafeLazy
import com.d4rk.qrcodescanner.plus.feature.BaseActivity
import com.d4rk.qrcodescanner.plus.model.Barcode
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import me.zhanghai.android.fastscroll.FastScrollerBuilder
class SaveBarcodeAsTextActivity : BaseActivity() {
    private lateinit var binding: ActivitySaveBarcodeAsTextBinding
    companion object {
        private const val REQUEST_PERMISSIONS_CODE = 101
        private val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        private const val BARCODE_KEY = "BARCODE_KEY"
        fun start(context: Context, barcode: Barcode) {
            val intent = Intent(context, SaveBarcodeAsTextActivity::class.java).apply {
                putExtra(BARCODE_KEY, barcode)
            }
            context.startActivity(intent)
        }
    }
    @Suppress("DEPRECATION")
    private val barcode by unsafeLazy {
        intent?.getSerializableExtra(BARCODE_KEY) as? Barcode ?: throw IllegalArgumentException("No barcode passed")
    }
    private val disposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveBarcodeAsTextBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportEdgeToEdge()
        initFormatSpinner()
        initSaveButton()
        FastScrollerBuilder(binding.scrollView).useMd2Style().build()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionsHelper.areAllPermissionsGranted(grantResults)) {
            saveBarcode()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
    private fun supportEdgeToEdge() {
        binding.rootView.applySystemWindowInsets(applyTop = true, applyBottom = true)
    }
    private fun initFormatSpinner() {
        binding.spinnerSaveAs.adapter = ArrayAdapter.createFromResource(this, R.array.activity_save_barcode_as_text_formats, R.layout.item_spinner).apply {
            setDropDownViewResource(R.layout.item_spinner_dropdown)
        }
    }
    private fun initSaveButton() {
        binding.buttonSave.setOnClickListener {
            requestPermissions()
        }
    }
    private fun requestPermissions() {
        permissionsHelper.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSIONS_CODE)
    }
    private fun saveBarcode() {
        val saveFunc = when (binding.spinnerSaveAs.selectedItemPosition) {
            0 -> barcodeSaver::saveBarcodeAsCsv
            1 -> barcodeSaver::saveBarcodeAsJson
            else -> return
        }
        showLoading(true)
        saveFunc(this, barcode).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    showBarcodeSaved()
                },
                { error ->
                    showLoading(false)
                    showError(error)
                }
            )
            .addTo(disposable)
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBarLoading.isVisible = isLoading
        binding.scrollView.isVisible = isLoading.not()
    }
    private fun showBarcodeSaved() {
        Snackbar.make(binding.root, R.string.snack_saved_to_downloads, Snackbar.LENGTH_LONG).show()
        finish()
    }
}