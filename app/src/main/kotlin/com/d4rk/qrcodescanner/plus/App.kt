package com.d4rk.qrcodescanner.plus
import androidx.multidex.MultiDexApplication
import com.d4rk.qrcodescanner.plus.di.settings
class App : MultiDexApplication() {
    override fun onCreate() {
        applyTheme()
        super.onCreate()
    }
    private fun applyTheme() {
        settings.reapplyTheme()
    }
}