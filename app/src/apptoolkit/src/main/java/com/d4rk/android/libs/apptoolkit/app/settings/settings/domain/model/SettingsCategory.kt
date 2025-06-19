package com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model

data class SettingsCategory(
    val title : String = "" , val preferences : List<SettingsPreference> = emptyList()
)