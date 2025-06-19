package com.d4rk.android.libs.apptoolkit.app.settings.general.domain.actions

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface GeneralSettingsEvent : UiEvent {
    data class Load(val contentKey : String?) : GeneralSettingsEvent
}