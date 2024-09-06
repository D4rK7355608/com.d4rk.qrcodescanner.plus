package com.d4rk.qrcodescanner.plus.ui.settings.camera

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.d4rk.qrcodescanner.plus.usecase.Settings

class ChooseCameraActivity : AppCompatActivity() {
    private lateinit var settings : Settings
    private var isBackCamera by mutableStateOf(value = false)

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        settings = Settings.getInstance(context = this)
        isBackCamera = settings.isBackCamera

        setContent {
            ChooseCameraScreen(isBackCamera = isBackCamera , onBackCameraChanged = { isChecked ->
                isBackCamera = isChecked
                settings.isBackCamera = isChecked
            })
        }
    }

    override fun onPause() {
        super.onPause()
        settings.isBackCamera = isBackCamera
    }

    override fun onResume() {
        super.onResume()
        isBackCamera = settings.isBackCamera
    }
}