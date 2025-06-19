package com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.fab

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick

@Composable
fun AnimatedFloatingActionButton(modifier : Modifier = Modifier , isVisible : Boolean , icon : ImageVector , contentDescription : String? = null , onClick : () -> Unit) {
    val view : View = LocalView.current

    AnimatedVisibility(visible = isVisible , enter = scaleIn() + fadeIn() , exit = scaleOut() + fadeOut()) {
        FloatingActionButton(onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            onClick()
        } , modifier = modifier.bounceClick()) {
            Icon(imageVector = icon , contentDescription = contentDescription)
        }
    }
}