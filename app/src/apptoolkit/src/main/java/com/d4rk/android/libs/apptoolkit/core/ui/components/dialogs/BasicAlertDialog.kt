package com.d4rk.android.libs.apptoolkit.core.ui.components.dialogs

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick

@Composable
fun BasicAlertDialog(
    onDismiss : () -> Unit ,
    onConfirm : () -> Unit ,
    onCancel : () -> Unit = onDismiss ,
    icon : ImageVector? = null ,
    iconTint : Color? = null ,
    title : String? = null ,
    content : @Composable () -> Unit = {} ,
    confirmButtonText : String? = null ,
    dismissButtonText : String? = null ,
    confirmEnabled : Boolean = true ,
    dismissEnabled : Boolean = true
) {
    val view : View = LocalView.current
    AlertDialog(onDismissRequest = onDismiss , icon = {
        if (icon != null) {
            Icon(
                imageVector = icon , contentDescription = null , tint = iconTint ?: LocalContentColor.current
            )
        }
    } , title = {
        if (! title.isNullOrEmpty()) {
            Text(text = title)
        }
    } , text = {
        content()
    } , confirmButton = {
        TextButton(modifier = Modifier.bounceClick() , onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            onConfirm()
        } , enabled = confirmEnabled) {
            Text(text = confirmButtonText ?: stringResource(id = android.R.string.ok))
        }
    } , dismissButton = {
        TextButton(modifier = Modifier.bounceClick() , onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            onCancel()
        } , enabled = dismissEnabled) {
            Text(text = dismissButtonText ?: stringResource(id = android.R.string.cancel))
        }
    })
}