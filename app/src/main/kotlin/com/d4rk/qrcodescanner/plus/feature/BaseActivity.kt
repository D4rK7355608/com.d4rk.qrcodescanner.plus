package com.d4rk.qrcodescanner.plus.feature
import android.os.Bundle
import android.view.View
import com.d4rk.qrcodescanner.plus.di.rotationHelper
import com.kieronquinn.monetcompat.app.MonetCompatActivity
abstract class BaseActivity : MonetCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        rotationHelper.lockCurrentOrientationIfNeeded(this)
    }
}