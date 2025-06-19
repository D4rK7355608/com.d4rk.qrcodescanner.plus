package com.d4rk.android.libs.apptoolkit.app.permissions.domain.actions

import android.content.Context
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface PermissionsEvent : UiEvent {
    data class Load(val context : Context) : PermissionsEvent
}
