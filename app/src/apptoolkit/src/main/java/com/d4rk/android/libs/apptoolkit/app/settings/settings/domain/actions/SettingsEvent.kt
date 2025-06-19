package com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.actions

import android.content.Context
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface SettingsEvent : UiEvent {
    data class Load(val context : Context) : SettingsEvent
}
