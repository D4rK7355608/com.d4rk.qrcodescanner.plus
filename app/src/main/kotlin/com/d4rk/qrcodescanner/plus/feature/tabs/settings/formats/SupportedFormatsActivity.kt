package com.d4rk.qrcodescanner.plus.feature.tabs.settings.formats
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.di.settings
import com.d4rk.qrcodescanner.plus.extension.applySystemWindowInsets
import com.d4rk.qrcodescanner.plus.extension.unsafeLazy
import com.d4rk.qrcodescanner.plus.feature.BaseActivity
import com.d4rk.qrcodescanner.plus.usecase.SupportedBarcodeFormats
import com.google.zxing.BarcodeFormat
import kotlinx.android.synthetic.main.activity_supported_formats.root_view
import kotlinx.android.synthetic.main.activity_supported_formats.recycler_view_formats
import kotlinx.android.synthetic.main.activity_supported_formats.toolbar
class SupportedFormatsActivity : BaseActivity(), FormatsAdapter.Listener {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SupportedFormatsActivity::class.java)
            context.startActivity(intent)
        }
    }
    private val formats by unsafeLazy { SupportedBarcodeFormats.FORMATS }
    private val formatSelection by unsafeLazy { formats.map(settings::isFormatSelected) }
    private val formatsAdapter by unsafeLazy { FormatsAdapter(this, formats, formatSelection) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supported_formats)
        supportEdgeToEdge()
        initRecyclerView()
        handleToolbarBackClicked()
    }
    override fun onFormatChecked(format: BarcodeFormat, isChecked: Boolean) {
        settings.setFormatSelected(format, isChecked)
    }
    private fun supportEdgeToEdge() {
        root_view.applySystemWindowInsets(applyTop = true, applyBottom = true)
    }
    private fun initRecyclerView() {
        recycler_view_formats.apply {
            layoutManager = LinearLayoutManager(this@SupportedFormatsActivity)
            adapter = formatsAdapter
        }
    }
    private fun handleToolbarBackClicked() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}