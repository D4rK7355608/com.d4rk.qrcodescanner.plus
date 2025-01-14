package com.d4rk.qrcodescanner.plus.ui.screens.settings.scanner.camera

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.d4rk.qrcodescanner.plus.ui.screens.settings.display.theme.style.AppTheme

class ChooseCameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize() , color = MaterialTheme.colorScheme.background
                ) {
                    _root_ide_package_.com.d4rk.qrcodescanner.plus.ui.screens.settings.scanner.camera.ChooseCameraScreen(
                        activity = this@ChooseCameraActivity
                    )
                }
            }
        }
    }
}