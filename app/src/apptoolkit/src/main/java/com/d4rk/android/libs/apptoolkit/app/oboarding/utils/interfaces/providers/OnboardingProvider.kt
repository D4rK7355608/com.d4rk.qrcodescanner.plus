package com.d4rk.android.libs.apptoolkit.app.oboarding.utils.interfaces.providers

import android.content.Context
import com.d4rk.android.libs.apptoolkit.app.oboarding.domain.data.model.ui.OnboardingPage

interface OnboardingProvider {
    fun getOnboardingPages(context: Context): List<OnboardingPage>
    fun onOnboardingFinished(context: Context)
}