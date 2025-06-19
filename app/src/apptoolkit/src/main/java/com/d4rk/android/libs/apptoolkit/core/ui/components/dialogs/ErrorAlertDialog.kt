package com.d4rk.android.libs.apptoolkit.core.ui.components.dialogs

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick

/**
 * Displays an error dialog with a given error message.
 *
 * @param errorMessage The message to be displayed in the error dialog.
 * @param onDismiss A callback function to be invoked when the dialog is dismissed (either by clicking the confirm button or tapping outside the dialog).
 */
@Composable
fun ErrorAlertDialog(
    errorMessage : String , onDismiss : () -> Unit
) {
    val view : View = LocalView.current
    AlertDialog(onDismissRequest = onDismiss , title = { Text(text = stringResource(id = R.string.error)) } , text = { Text(text = errorMessage) } , confirmButton = {
        TextButton(modifier = Modifier.bounceClick(), onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            onDismiss()
        }) {
            Text(text = stringResource(id = android.R.string.ok))
        }
    })
}