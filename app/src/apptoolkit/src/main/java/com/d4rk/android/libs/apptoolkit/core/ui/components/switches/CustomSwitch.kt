package com.d4rk.android.libs.apptoolkit.core.ui.components.switches

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val view: View = LocalView.current

    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = {
            onCheckedChange(it)
            view.playSoundEffect(SoundEffectConstants.CLICK)
        },
        thumbContent = {
            if (checked) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(size = SizeConstants.SwitchIconSize)
                )
            } else {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier.size(size = SizeConstants.SwitchIconSize)
                )
            }
        }
    )
}