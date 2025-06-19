package com.d4rk.android.libs.apptoolkit.core.utils.interfaces

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

interface DismissibleSnackbarViewModel<E : UiEvent> {
    fun getDismissSnackbarEvent() : E
}