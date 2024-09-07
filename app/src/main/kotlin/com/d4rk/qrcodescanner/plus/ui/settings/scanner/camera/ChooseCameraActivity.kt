package com.d4rk.qrcodescanner.plus.ui.settings.scanner.camera

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.d4rk.qrcodescanner.plus.ui.settings.display.theme.style.AppTheme
import com.d4rk.qrcodescanner.plus.usecase.Settings

class ChooseCameraActivity : AppCompatActivity() {
    private lateinit var settings : Settings
    private var isBackCamera by mutableStateOf(value = false)

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        settings = Settings.getInstance(context = this)
        isBackCamera = settings.isBackCamera
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize() , color = MaterialTheme.colorScheme.background
                ) {
                    ChooseCameraScreen(isBackCamera = isBackCamera ,
                                       onBackCameraChanged = { isChecked ->
                                           isBackCamera = isChecked
                                           settings.isBackCamera = isChecked
                                       } ,
                                       activity = this@ChooseCameraActivity)
                }
            }
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