package com.d4rk.qrcodescanner.plus.feature.tile
import android.content.Intent
import android.service.quicksettings.TileService
import com.d4rk.qrcodescanner.plus.feature.tabs.BottomTabsActivity
class QuickSettingsTileService : TileService() {
    override fun onClick() {
        super.onClick()
        val intent = Intent(applicationContext, BottomTabsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivityAndCollapse(intent)
    }
}