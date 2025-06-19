package com.d4rk.android.libs.apptoolkit.app.oboarding.domain.data.model.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class OnboardingPage {

    data class DefaultPage(
        val key: String,
        val title: String,
        val description: String,
        val imageVector: ImageVector,
        val isEnabled: Boolean = true
    ) : OnboardingPage()

    data class CustomPage(
        val key: String,
        val content: @Composable () -> Unit,
        val isEnabled: Boolean = true
    ) : OnboardingPage()
}