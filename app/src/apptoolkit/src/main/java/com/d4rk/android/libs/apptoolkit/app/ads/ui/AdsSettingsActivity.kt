package com.d4rk.android.libs.apptoolkit.app.ads.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.app.theme.style.AppTheme
import org.koin.android.ext.android.inject

class AdsSettingsActivity : AppCompatActivity() {
    private val buildInfoProvider : BuildInfoProvider by inject()
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize() , color = MaterialTheme.colorScheme.background) {
                    AdsSettingsScreen(activity = this@AdsSettingsActivity , buildInfoProvider = buildInfoProvider)
                }
            }
        }
    }
}