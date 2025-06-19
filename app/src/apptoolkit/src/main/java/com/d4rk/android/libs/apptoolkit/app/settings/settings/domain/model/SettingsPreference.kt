package com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class SettingsPreference(
    val key : String? = null , val icon : ImageVector? = null , val title : String? = null , val summary : String? = null , val action : () -> Unit = {}
)