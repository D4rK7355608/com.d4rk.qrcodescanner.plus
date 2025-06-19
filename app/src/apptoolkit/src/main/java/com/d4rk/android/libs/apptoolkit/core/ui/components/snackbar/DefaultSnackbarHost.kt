package com.d4rk.android.libs.apptoolkit.core.ui.components.snackbar

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.CustomSnackbarVisuals
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun DefaultSnackbarHost(snackbarState : SnackbarHostState , modifier : Modifier = Modifier) {
    val view : View = LocalView.current
    SnackbarHost(hostState = snackbarState , modifier = modifier) { snackbarData : SnackbarData ->
        (snackbarData.visuals as? CustomSnackbarVisuals)?.let { visuals : CustomSnackbarVisuals ->
            val isError : Boolean = visuals.isError

            Snackbar(
                modifier = Modifier.padding(all = SizeConstants.LargeSize) ,
                containerColor = if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.inverseSurface ,
                contentColor = if (isError) MaterialTheme.colorScheme.error else SnackbarDefaults.contentColor ,
                action = {
                    IconButton(modifier = Modifier.bounceClick() , onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        snackbarData.dismiss()
                    }) {
                        Icon(imageVector = Icons.Outlined.Close , contentDescription = "Close Snackbar" , tint = if (isError) MaterialTheme.colorScheme.error else SnackbarDefaults.contentColor)
                    }
                }) {
                Text(text = visuals.message)
            }
        }
    }
}