package com.d4rk.qrcodescanner.plus.feature.tile

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import com.d4rk.qrcodescanner.plus.MainActivity

class QuickSettingsTileService : TileService() {

    @SuppressLint("StartActivityAndCollapseDeprecated")
    override fun onClick() {
        super.onClick()
        val intent = Intent(applicationContext , MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        }
        else {
            0
        }
        val pendingIntent = PendingIntent.getActivity(this , 0 , intent , pendingIntentFlags)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startActivityAndCollapse(pendingIntent)
        }
        else {
            @Suppress("DEPRECATION") startActivityAndCollapse(intent)
        }
    }
}