package com.d4rk.android.libs.apptoolkit.app.oboarding.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class OnboardingViewModel : ViewModel() {
    var currentTabIndex by mutableStateOf(0)
}
