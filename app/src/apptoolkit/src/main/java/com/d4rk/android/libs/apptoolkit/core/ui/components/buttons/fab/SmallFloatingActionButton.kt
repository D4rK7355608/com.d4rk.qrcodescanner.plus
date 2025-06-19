package com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.fab

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick

/**
 * A customizable small floating action button that animates its visibility.
 *
 * This composable provides a small floating action button with animated visibility, scaling in and out.
 * It also includes a click sound effect and a bounce click animation.
 *
 * @param modifier Modifier to be applied to the button.
 * @param isVisible Controls the visibility of the button. The button is only visible if this is true.
 * @param isExtended Controls if the button is extended. The button is only visible if this is true.
 * @param icon The icon to be displayed inside the button.
 * @param contentDescription Optional description of the icon for accessibility.
 * @param onClick The action to be performed when the button is clicked.
 */
@Composable
fun SmallFloatingActionButton(modifier : Modifier = Modifier , isVisible : Boolean , isExtended : Boolean , icon : ImageVector , contentDescription : String? = null , onClick : () -> Unit) {
    val view : View = LocalView.current

    AnimatedVisibility(
        visible = isVisible && isExtended ,
        enter = scaleIn() ,
        exit = scaleOut() ,
    ) {
        SmallFloatingActionButton(onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            onClick()
        } , modifier = modifier.bounceClick()) {
            Icon(imageVector = icon , contentDescription = contentDescription)
        }
    }
}