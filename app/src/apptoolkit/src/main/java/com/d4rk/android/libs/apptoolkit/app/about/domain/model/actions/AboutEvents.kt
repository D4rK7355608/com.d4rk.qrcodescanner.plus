package com.d4rk.android.libs.apptoolkit.app.about.domain.model.actions

import android.content.Context
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed class AboutEvents : UiEvent {
    data object CopyDeviceInfo : AboutEvents()
    data object DismissSnackbar : AboutEvents()
    data class LoadHtml(val context : Context , val packageName : String , val versionName : String) : AboutEvents()
}
