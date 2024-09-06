package com.d4rk.qrcodescanner.plus.ui.settings.search

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.d4rk.qrcodescanner.plus.di.settings

class ChooseSearchEngineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChooseSearchEngineScreen(settings = settings)
        }
    }
}