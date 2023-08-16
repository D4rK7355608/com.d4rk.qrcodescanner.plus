package com.d4rk.qrcodescanner.plus.ui.scan.file
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.ext.SdkExtensions
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent.ACTION_UP
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.ActivityScanBarcodeFromFileBinding
import com.d4rk.qrcodescanner.plus.di.barcodeDatabase
import com.d4rk.qrcodescanner.plus.di.barcodeImageScanner
import com.d4rk.qrcodescanner.plus.di.barcodeParser
import com.d4rk.qrcodescanner.plus.di.permissionsHelper
import com.d4rk.qrcodescanner.plus.di.settings
import com.d4rk.qrcodescanner.plus.extension.applySystemWindowInsets
import com.d4rk.qrcodescanner.plus.extension.showError
import com.d4rk.qrcodescanner.plus.feature.BaseActivity
import com.d4rk.qrcodescanner.plus.feature.barcode.BarcodeActivity
import com.d4rk.qrcodescanner.plus.model.Barcode
import com.d4rk.qrcodescanner.plus.usecase.save
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.Result
import com.jakewharton.rxbinding2.view.touches
import com.mayank.simplecropview.CropImageView
import com.mayank.simplecropview.callback.LoadCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
class ScanBarcodeFromFileActivity : BaseActivity() {
    private lateinit var binding: ActivityScanBarcodeFromFileBinding
    private lateinit var pickMediaLauncher: ActivityResultLauncher<Intent>
    companion object {
        private const val CHOOSE_FILE_REQUEST_CODE = 12
        private const val PERMISSIONS_REQUEST_CODE = 14
        private val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        fun start(context: Context) {
            val intent = Intent(context, ScanBarcodeFromFileActivity::class.java)
            context.startActivity(intent)
        }
    }
    private var lastScanResult: Result? = null
    private val disposable = CompositeDisposable()
    private val scanDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBarcodeFromFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selectImage()
        supportEdgeToEdge()
        handleImageCropAreaChanged()
        handleScanButtonClicked()
    }
    override fun onDestroy() {
        super.onDestroy()
        scanDisposable.clear()
        disposable.clear()
    }
    private fun supportEdgeToEdge() {
        binding.rootView.applySystemWindowInsets(applyTop = true, applyBottom = true)
    }
    private fun showErrorOrRequestPermissions(error: Throwable) {
        when (error) {
            is SecurityException -> permissionsHelper.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            else -> showError(error)
        }
    }
    private fun selectImage() {
        pickMediaLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != Activity.RESULT_OK) {
                    Snackbar.make(binding.root, "Failed to retrieve media.", Snackbar.LENGTH_SHORT).show()
                } else {
                    val uri = it.data?.data
                    if (uri != null) {
                        binding.cropImageView.load(uri)
                            .execute(object : LoadCallback {
                                override fun onSuccess() {
                                    scanCroppedImage()
                                    binding.cropImageView.invalidate()
                                }
                                override fun onError(e: Throwable) {
                                    showErrorOrRequestPermissions(e)
                                }
                            })
                    }
                }
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(Build.VERSION_CODES.R) >= 2) {
            pickMediaLauncher.launch(
                Intent(MediaStore.ACTION_PICK_IMAGES)
                    .apply {
                        type = "image/*"
                    }
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            @Suppress("DEPRECATION")
            startActivityForResult(intent, CHOOSE_FILE_REQUEST_CODE)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scan_barcode_from_image, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_rotate_left -> {
                binding.cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D)
                true
            }
            R.id.item_rotate_right -> {
                binding.cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D)
                true
            }
            R.id.item_change_image -> {
                selectImage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun handleImageCropAreaChanged() {
        binding.cropImageView.touches()
            .filter { it.action == ACTION_UP }
            .debounce(400, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { scanCroppedImage() },
                { error -> showError(error) }
            )
            .addTo(disposable)
    }
    private fun handleScanButtonClicked() {
        binding.buttonScan.setOnClickListener {
            if (lastScanResult != null) {
                saveScanResult()
            }
        }
    }
    private fun scanCroppedImage() {
        scanDisposable.clear()
        lastScanResult = null
        scanCroppedImage(binding.cropImageView.croppedBitmap)
    }
    private fun scanCroppedImage(image: Bitmap) {
        barcodeImageScanner
            .parse(image)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { scanResult ->
                    lastScanResult = scanResult
                },
                { error -> showError(error) }
            )
            .addTo(scanDisposable)
    }
    private fun saveScanResult() {
        val barcode = lastScanResult?.let(barcodeParser::parseResult) ?: return
        if (settings.saveScannedBarcodesToHistory.not()) {
            navigateToBarcodeScreen(barcode)
            return
        }
        barcodeDatabase.save(barcode, settings.doNotSaveDuplicates)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { id ->
                    navigateToBarcodeScreen(barcode.copy(id = id))
                },
                { error ->
                    showError(error)
                }
            )
            .addTo(disposable)
    }
    private fun navigateToBarcodeScreen(barcode: Barcode) {
        BarcodeActivity.start(this, barcode)
        finish()
    }
}