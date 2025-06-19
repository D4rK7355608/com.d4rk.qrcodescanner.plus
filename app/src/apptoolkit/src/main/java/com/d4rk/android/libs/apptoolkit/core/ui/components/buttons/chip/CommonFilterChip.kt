package com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.chip

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick

@Composable
fun CommonFilterChip(selected : Boolean , onClick : () -> Unit , label : String , modifier : Modifier = Modifier , leadingIcon : (@Composable (() -> Unit))? = null) {
    FilterChip(selected = selected , onClick = onClick , label = { Text(text = label) } , leadingIcon = {
        if (leadingIcon != null) {
            leadingIcon()
        }
        else {
            AnimatedContent(targetState = selected) { targetChecked : Boolean ->
                if (targetChecked) {
                    Icon(imageVector = Icons.Filled.Check , contentDescription = null)
                }
            }
        }
    } , modifier = modifier.bounceClick())
}