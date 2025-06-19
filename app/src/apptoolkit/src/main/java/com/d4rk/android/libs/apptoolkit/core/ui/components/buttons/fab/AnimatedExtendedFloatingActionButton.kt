package com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.fab

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick

/**
 * An animated extended floating action button that scales in and out based on its visibility.
 *
 * @param visible Controls the visibility of the button. When true, the button is fully visible; when false, it's scaled down to nothing.
 * @param onClick The action to perform when the button is clicked.
 * @param icon The icon to display within the button.
 * @param text Optional text to display alongside the icon in the button.
 * @param expanded Determines whether the button is in its expanded state (with text) or collapsed (icon only).
 * @param modifier Modifier to apply to the button.
 */
@Composable
fun AnimatedExtendedFloatingActionButton(
    visible : Boolean = true , onClick : () -> Unit , icon : @Composable () -> Unit , text : (@Composable () -> Unit)? = null , expanded : Boolean = true , modifier : Modifier = Modifier , containerColor : Color = FloatingActionButtonDefaults.containerColor
) {
    val animatedScale : Float by animateFloatAsState(
        targetValue = if (visible) 1f else 0f , animationSpec = tween(durationMillis = 400 , easing = FastOutSlowInEasing) , label = "FAB Scale"
    )

    val view : View = LocalView.current

    if (animatedScale > 0f) {
        ExtendedFloatingActionButton(onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            onClick()
        } , icon = icon , text = text ?: {} , expanded = expanded , modifier = modifier
                .graphicsLayer {
                    scaleX = animatedScale
                    scaleY = animatedScale
                    transformOrigin = TransformOrigin(pivotFractionX = 1f , pivotFractionY = 1f)
                }
                .bounceClick() , containerColor = containerColor)
    }
}