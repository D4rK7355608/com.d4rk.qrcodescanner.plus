package com.d4rk.qrcodescanner.plus.ui.settings.formats

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.d4rk.qrcodescanner.plus.di.settings

class SupportedFormatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SupportedFormatsScreen(settings = settings)
        }
    }
}