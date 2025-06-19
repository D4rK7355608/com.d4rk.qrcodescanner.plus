package com.d4rk.qrcodescanner.plus.extension
import android.content.ClipboardManager
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Vibrator
import java.util.Locale
@Suppress("DEPRECATION")
val Context.vibrator: Vibrator? get() = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
val Context.wifiManager: WifiManager? get() = applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
val Context.clipboardManager: ClipboardManager? get() = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
val Context.currentLocale: Locale? get() = resources.configuration.locales.get(0)