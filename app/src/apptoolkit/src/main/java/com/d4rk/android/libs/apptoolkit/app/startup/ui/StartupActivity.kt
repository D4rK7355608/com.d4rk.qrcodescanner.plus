package com.d4rk.android.libs.apptoolkit.app.startup.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.d4rk.android.libs.apptoolkit.app.startup.utils.interfaces.providers.StartupProvider
import com.d4rk.android.libs.apptoolkit.app.theme.style.AppTheme
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ConsentFormHelper
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import com.google.android.ump.ConsentInformation
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class StartupActivity : AppCompatActivity() {
    private val provider : StartupProvider by inject()
    var consentFormLoaded : Boolean = false
    private val permissionLauncher : ActivityResultLauncher<Array<String>> = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize() , color = MaterialTheme.colorScheme.background) {
                    StartupScreen(activity = this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (provider.requiredPermissions.isNotEmpty()) {
            permissionLauncher.launch(input = provider.requiredPermissions)
        }

        checkUserConsent()
    }

    fun navigateToNext() {
        lifecycleScope.launch {
            IntentsHelper.openActivity(context = this@StartupActivity , activityClass = provider.getNextIntent(this@StartupActivity).component?.className?.let {
                Class.forName(it)
            } ?: StartupActivity::class.java)
            finish()
        }
    }

    private fun checkUserConsent() {
        val consentInfo: ConsentInformation = UserMessagingPlatform.getConsentInformation(this)
        ConsentFormHelper.showConsentFormIfRequired(activity = this , consentInfo = consentInfo)
        consentFormLoaded = true
    }
}