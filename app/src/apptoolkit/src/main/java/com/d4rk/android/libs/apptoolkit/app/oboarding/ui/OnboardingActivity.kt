package com.d4rk.android.libs.apptoolkit.app.oboarding.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.app.theme.style.AppTheme
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ConsentFormHelper
import com.google.android.ump.ConsentInformation
import com.google.android.ump.UserMessagingPlatform

class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OnboardingScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkUserConsent()
        // Recheck permissions when returning to this activity
    }

    private fun checkUserConsent() {
        val consentInfo: ConsentInformation = UserMessagingPlatform.getConsentInformation(this)
        ConsentFormHelper.showConsentFormIfRequired(activity = this , consentInfo = consentInfo)
    }
}
