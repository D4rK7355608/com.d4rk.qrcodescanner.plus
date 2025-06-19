package com.d4rk.android.libs.apptoolkit.app.help.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.HelpScreenConfig
import com.d4rk.android.libs.apptoolkit.app.theme.style.AppTheme
import org.koin.android.ext.android.inject

class HelpActivity : AppCompatActivity() {
    private val config : HelpScreenConfig by inject()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize() , color = MaterialTheme.colorScheme.background) {
                    HelpScreen(activity = this@HelpActivity , config = config)
                }
            }
        }
    }
}