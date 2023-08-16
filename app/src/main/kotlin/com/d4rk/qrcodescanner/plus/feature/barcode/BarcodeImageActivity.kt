package com.d4rk.qrcodescanner.plus.feature.barcode
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.ActivityBarcodeImageBinding
import com.d4rk.qrcodescanner.plus.di.barcodeImageGenerator
import com.d4rk.qrcodescanner.plus.di.settings
import com.d4rk.qrcodescanner.plus.extension.applySystemWindowInsets
import com.d4rk.qrcodescanner.plus.extension.toStringId
import com.d4rk.qrcodescanner.plus.extension.unsafeLazy
import com.d4rk.qrcodescanner.plus.feature.BaseActivity
import com.d4rk.qrcodescanner.plus.model.Barcode
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import java.text.SimpleDateFormat
import java.util.Locale
class BarcodeImageActivity : BaseActivity() {
    companion object {
        private const val BARCODE_KEY = "BARCODE_KEY"
        fun start(context: Context, barcode: Barcode) {
            val intent = Intent(context, BarcodeImageActivity::class.java)
            intent.putExtra(BARCODE_KEY, barcode)
            context.startActivity(intent)
        }
    }
    private lateinit var binding: ActivityBarcodeImageBinding
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH)
    @Suppress("DEPRECATION")
    private val barcode by unsafeLazy {
        intent?.getSerializableExtra(BARCODE_KEY) as? Barcode ?: throw IllegalArgumentException("No barcode passed")
    }
    private var originalBrightness: Float = 0.5f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportEdgeToEdge()
        saveOriginalBrightness()
        handleToolbarBackPressed()
        handleToolbarMenuItemClicked()
        showMenu()
        showBarcode()
        FastScrollerBuilder(binding.scrollView).useMd2Style().build()
    }
    private fun supportEdgeToEdge() {
        binding.rootView.applySystemWindowInsets(applyTop = true, applyBottom = true)
    }
    private fun saveOriginalBrightness() {
        originalBrightness = window.attributes.screenBrightness
    }
    private fun handleToolbarBackPressed() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    private fun handleToolbarMenuItemClicked() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_increase_brightness -> {
                    increaseBrightnessToMax()
                    binding.toolbar.menu.apply {
                        findItem(R.id.item_increase_brightness).isVisible = false
                        findItem(R.id.item_decrease_brightness).isVisible = true
                    }
                }
                R.id.item_decrease_brightness -> {
                    restoreOriginalBrightness()
                    binding.toolbar.menu.apply {
                        findItem(R.id.item_decrease_brightness).isVisible = false
                        findItem(R.id.item_increase_brightness).isVisible = true
                    }
                }
            }
            return@setOnMenuItemClickListener true
        }
    }
    private fun showMenu() {
        binding.toolbar.inflateMenu(R.menu.menu_barcode_image)
    }
    private fun showBarcode() {
        showBarcodeImage()
        showBarcodeDate()
        showBarcodeFormat()
        showBarcodeText()
    }
    private fun showBarcodeImage() {
        try {
            val bitmap = barcodeImageGenerator.generateBitmap(barcode, 2000, 2000, 0, settings.barcodeContentColor, settings.barcodeBackgroundColor)
            binding.imageViewBarcode.setImageBitmap(bitmap)
            binding.imageViewBarcode.setBackgroundColor(settings.barcodeBackgroundColor)
            binding.layoutBarcodeImageBackground.setBackgroundColor(settings.barcodeBackgroundColor)
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO || settings.areBarcodeColorsInversed) {
                binding.layoutBarcodeImageBackground.setPadding(0, 0, 0, 0)
            }
        } catch (ex: Exception) {
            binding.imageViewBarcode.isVisible = false
        }
    }
    private fun showBarcodeDate() {
        binding.textViewDate.text = dateFormatter.format(barcode.date)
    }
    private fun showBarcodeFormat() {
        val format = barcode.format.toStringId()
        binding.toolbar.setTitle(format)
    }
    private fun showBarcodeText() {
        binding.textViewBarcodeText.text = barcode.text
    }
    private fun increaseBrightnessToMax() {
        setBrightness(1.0f)
    }
    private fun restoreOriginalBrightness() {
        setBrightness(originalBrightness)
    }
    private fun setBrightness(brightness: Float) {
        window.attributes = window.attributes.apply {
            screenBrightness = brightness
        }
    }
}