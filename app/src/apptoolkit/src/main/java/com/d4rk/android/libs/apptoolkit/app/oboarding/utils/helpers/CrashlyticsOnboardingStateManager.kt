package com.d4rk.android.libs.apptoolkit.app.oboarding.utils.helpers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CrashlyticsOnboardingStateManager {
    companion object {
        var showCrashlyticsDialog by mutableStateOf(true)
            private set

        fun openDialog() {
            showCrashlyticsDialog = true
        }

        fun dismissDialog() {
            showCrashlyticsDialog = false
        }
    }
}