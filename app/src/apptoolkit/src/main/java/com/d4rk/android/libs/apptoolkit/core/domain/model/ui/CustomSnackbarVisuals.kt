package com.d4rk.android.libs.apptoolkit.core.domain.model.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

data class CustomSnackbarVisuals(
    override val message : String , override val actionLabel : String? = null , override val withDismissAction : Boolean = false , override val duration : SnackbarDuration = SnackbarDuration.Short , val isError : Boolean = false
) : SnackbarVisuals