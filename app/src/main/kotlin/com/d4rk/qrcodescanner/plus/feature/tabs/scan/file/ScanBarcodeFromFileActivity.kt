package com.d4rk.qrcodescanner.plus.feature.tabs.scan.file
import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.view.MotionEvent.ACTION_UP
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.ActivityScanBarcodeFromFileBinding
import com.d4rk.qrcodescanner.plus.di.settings
import com.d4rk.qrcodescanner.plus.di.barcodeParser
import com.d4rk.qrcodescanner.plus.di.permissionsHelper
import com.d4rk.qrcodescanner.plus.di.barcodeImageScanner
import com.d4rk.qrcodescanner.plus.di.barcodeDatabase
import com.d4rk.qrcodescanner.plus.extension.applySystemWindowInsets
import com.d4rk.qrcodescanner.plus.extension.showError
import com.d4rk.qrcodescanner.plus.feature.BaseActivity
import com.d4rk.qrcodescanner.plus.feature.barcode.BarcodeActivity
import com.d4rk.qrcodescanner.plus.model.Barcode
import com.d4rk.qrcodescanner.plus.usecase.save
import com.google.zxing.Result
import com.isseiaoki.simplecropview.CropImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
class ScanBarcodeFromFileActivity : BaseActivity() {
    private lateinit var binding: ActivityScanBarcodeFromFileBinding
    companion object {
        private const val CHOOSE_FILE_REQUEST_CODE = 12
        private const val CHOOSE_FILE_AGAIN_REQUEST_CODE = 13
        private const val PERMISSIONS_REQUEST_CODE = 14
        private val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        fun start(context: Context) {
            val intent = Intent(context, ScanBarcodeFromFileActivity::class.java)
            context.startActivity(intent)
        }
    }
    private var imageUri: Uri? = null
    private var lastScanResult: Result? = null
    private val disposable = CompositeDisposable()
    private val scanDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBarcodeFromFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportEdgeToEdge()
        handleToolbarBackPressed()
        handleToolbarMenuItemClicked()
        handleImageCropAreaChanged()
        handleScanButtonClicked()
        if (showImageFromIntent().not()) {
            startChooseImageActivity(savedInstanceState)
        }
    }
    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == CHOOSE_FILE_REQUEST_CODE || requestCode == CHOOSE_FILE_AGAIN_REQUEST_CODE) && resultCode == RESULT_OK) {
            data?.data?.apply(::showImage)
            return
        }
        if (requestCode == CHOOSE_FILE_REQUEST_CODE) {
            finish()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE && permissionsHelper.areAllPermissionsGranted(grantResults)) {
            imageUri?.apply(::showImage)
        } else {
            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        scanDisposable.clear()
        disposable.clear()
    }
    private fun supportEdgeToEdge() {
        binding.rootView.applySystemWindowInsets(applyTop = true, applyBottom = true)
    }
    private fun showImageFromIntent(): Boolean {
        var uri: Uri? = null
        if (intent?.action == Intent.ACTION_SEND && intent.type.orEmpty().startsWith("image/")) {
            uri = intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri
        }
        if (intent?.action == Intent.ACTION_VIEW && intent.type.orEmpty().startsWith("image/")) {
            uri = intent.data
        }
        if (uri == null) {
            return false
        }
        showImage(uri)
        return true
    }
    private fun startChooseImageActivity(savedInstanceState: Bundle?) {
        startChooseImageActivity(CHOOSE_FILE_REQUEST_CODE, savedInstanceState)
    }
    private fun startChooseImageActivityAgain() {
        startChooseImageActivity(CHOOSE_FILE_AGAIN_REQUEST_CODE, null)
    }
    @Suppress("DEPRECATION")
    private fun startChooseImageActivity(requestCode: Int, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            return
        }
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, requestCode)
        }
    }
    private fun handleToolbarBackPressed() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    private fun handleToolbarMenuItemClicked() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_rotate_left -> binding.cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D)
                R.id.item_rotate_right -> binding.cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D)
                R.id.item_change_image -> startChooseImageActivityAgain()
            }
            return@setOnMenuItemClickListener true
        }
    }
    private fun handleImageCropAreaChanged() {
        binding.cropImageView.touches()
            .filter { it.action == ACTION_UP }
            .debounce(400, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { scanCroppedImage() }
            .addTo(disposable)
    }
    private fun handleScanButtonClicked() {
        binding.buttonScan.setOnClickListener {
            saveScanResult()
        }
    }
    private fun showImage(imageUri: Uri) {
        this.imageUri = imageUri
        binding.cropImageView
            .load(imageUri)
            .executeAsCompletable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { scanCroppedImage() },
                ::showErrorOrRequestPermissions
            )
            .addTo(disposable)
    }
    private fun showErrorOrRequestPermissions(error: Throwable) {
        when (error) {
            is SecurityException -> permissionsHelper.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            else -> showError(error)
        }
    }
    private fun scanCroppedImage() {
        showLoading(true)
        showScanButtonEnabled(false)
        scanDisposable.clear()
        lastScanResult = null
        binding.cropImageView
            .cropAsSingle()
            .subscribeOn(Schedulers.io())
            .subscribe(::scanCroppedImage, ::showError)
            .addTo(scanDisposable)
    }
    private fun scanCroppedImage(image: Bitmap) {
        barcodeImageScanner
            .parse(image)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { scanResult ->
                    lastScanResult = scanResult
                    showScanButtonEnabled(true)
                    showLoading(false)
                },
                { showLoading(false) }
            )
            .addTo(scanDisposable)
    }
    private fun saveScanResult() {
        val barcode = lastScanResult?.let(barcodeParser::parseResult) ?: return
        if (settings.saveScannedBarcodesToHistory.not()) {
            navigateToBarcodeScreen(barcode)
            return
        }
        showLoading(true)
        barcodeDatabase.save(barcode, settings.doNotSaveDuplicates)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { id ->
                    navigateToBarcodeScreen(barcode.copy(id = id))
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
        binding.buttonScan.isInvisible = isLoading
    }
    private fun showScanButtonEnabled(isEnabled: Boolean) {
        binding.buttonScan.isEnabled = isEnabled
    }
    private fun navigateToBarcodeScreen(barcode: Barcode) {
        BarcodeActivity.start(this, barcode)
        finish()
    }
}