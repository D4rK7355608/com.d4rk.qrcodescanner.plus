package com.d4rk.android.libs.apptoolkit.app.settings.utils.interfaces

import android.content.Context
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig

interface SettingsProvider {
    fun provideSettingsConfig(context : Context) : SettingsConfig
}