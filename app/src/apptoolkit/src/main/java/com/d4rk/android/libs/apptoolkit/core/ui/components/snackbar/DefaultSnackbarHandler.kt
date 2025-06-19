package com.d4rk.android.libs.apptoolkit.core.ui.components.snackbar

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.CustomSnackbarVisuals
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

@Composable
fun <T , E : UiEvent> DefaultSnackbarHandler(screenState : UiStateScreen<T> , snackbarHostState : SnackbarHostState , getDismissEvent : (() -> E)? = null , onEvent : ((E) -> Unit)? = null) {
    val context : Context = LocalContext.current

    LaunchedEffect(key1 = screenState.snackbar?.timeStamp) {
        screenState.snackbar?.let { snackbar : UiSnackbar ->
            if (snackbarHostState.currentSnackbarData != null) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }

            val result : SnackbarResult = snackbarHostState.showSnackbar(visuals = CustomSnackbarVisuals(message = snackbar.message.asString(context) , withDismissAction = true , duration = if (snackbar.isError) SnackbarDuration.Long else SnackbarDuration.Short , isError = snackbar.isError))
            if ((result == SnackbarResult.Dismissed || result == SnackbarResult.ActionPerformed) && getDismissEvent != null && onEvent != null) {
                onEvent(getDismissEvent())
            }
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}