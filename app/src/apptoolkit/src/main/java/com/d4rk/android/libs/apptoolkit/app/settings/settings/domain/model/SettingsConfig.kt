package com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model

data class SettingsConfig(
    val title : String = "Settings" , val categories : List<SettingsCategory> = emptyList()
)