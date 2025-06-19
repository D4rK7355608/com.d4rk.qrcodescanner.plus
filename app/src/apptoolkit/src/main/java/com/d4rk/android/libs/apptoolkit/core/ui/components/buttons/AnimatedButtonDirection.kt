package com.d4rk.android.libs.apptoolkit.core.ui.components.buttons

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick

/**
 * An animated button that slides in and out horizontally with a fade effect.
 *
 * This composable provides an animated button that appears and disappears with a slide and fade animation.
 * The animation direction (from the left or right) and duration can be customized.
 *
 * @param modifier Modifier to be applied to the IconButton.
 * @param visible Controls the visibility of the button. If true, the button will be visible, otherwise it will be hidden.
 * @param icon The icon to display within the button.
 * @param contentDescription The content description for the icon, used for accessibility.
 * @param onClick The callback that will be invoked when the button is clicked.
 * @param durationMillis The duration of the animation in milliseconds. Defaults to 500ms.
 * @param autoAnimate If true, the button will automatically animate in when `visible` is true.
 *                    If false, the animation will not be triggered automatically and will only occur when the visibility state changes. Defaults to true.
 * @param fromRight If true, the button will slide in from the right and slide out to the right.
 *                  If false, the button will slide in from the left and slide out to the left. Defaults to false.
 *
 * Example Usage:
 * ```
 *  AnimatedButtonDirection(
 *      modifier = Modifier.padding(16.dp),
 *      visible = is */
@Composable
fun AnimatedButtonDirection(
    modifier : Modifier = Modifier , visible : Boolean = true , icon : ImageVector , contentDescription : String? , onClick : () -> Unit , durationMillis : Int = 500 , autoAnimate : Boolean = true , fromRight : Boolean = false
) {
    val animatedVisibility : MutableState<Boolean> = rememberSaveable { mutableStateOf(value = false) }

    val view : View = LocalView.current

    LaunchedEffect(visible) {
        if (autoAnimate && visible) {
            animatedVisibility.value = true
        }
        else if (! visible) {
            animatedVisibility.value = false
        }
    }

    AnimatedVisibility(
        visible = animatedVisibility.value && visible ,
                       enter = fadeIn(animationSpec = tween(durationMillis = durationMillis)) + slideInHorizontally(initialOffsetX = { if (fromRight) it else - it } , animationSpec = tween(durationMillis = durationMillis)) ,
                       exit = fadeOut(animationSpec = tween(durationMillis = durationMillis)) + slideOutHorizontally(targetOffsetX = { if (fromRight) it else - it } , animationSpec = tween(durationMillis = durationMillis))) {
        IconButton(modifier = modifier.bounceClick() , onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            onClick()
        }) {
            Icon(imageVector = icon , contentDescription = contentDescription)
        }
    }
}