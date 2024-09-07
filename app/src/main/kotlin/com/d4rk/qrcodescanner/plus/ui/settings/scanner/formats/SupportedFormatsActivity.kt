package com.d4rk.qrcodescanner.plus.ui.settings.scanner.formats

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.d4rk.qrcodescanner.plus.di.settings
import com.d4rk.qrcodescanner.plus.ui.settings.display.theme.style.AppTheme

class SupportedFormatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize() , color = MaterialTheme.colorScheme.background
                ) {
                    SupportedFormatsScreen(activity = this@SupportedFormatsActivity, settings = settings)
                }
            }
        }
    }
}