package com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import com.d4rk.android.libs.apptoolkit.core.domain.model.animations.button.ButtonState
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore

/**
 * A modifier that adds a bounce effect to a composable when it's clicked.
 *
 * This modifier uses a scale animation to simulate a "bounce" effect when the composable
 * is pressed. The animation is only applied if bouncy buttons are enabled in the [CommonDataStore] and
 * animationEnabled is true.
 *
 * @param animationEnabled Whether the animation is enabled. Default is true.
 * @return A [Modifier] that applies the bounce effect on click.
 */
@Composable
fun Modifier.bounceClick(
    animationEnabled : Boolean = true ,
) : Modifier = composed {
    var buttonState : ButtonState by remember { mutableStateOf(value = ButtonState.Idle) }
    val context: Context = LocalContext.current
    val dataStore: CommonDataStore = CommonDataStore.getInstance(context = context)
    val bouncyButtonsEnabled : Boolean by dataStore.bouncyButtons.collectAsState(initial = true)
    val scale : Float by animateFloatAsState(
        if (buttonState == ButtonState.Pressed && animationEnabled && bouncyButtonsEnabled) 0.96f else 1f , label = "Button Press Scale Animation"
    )

    if (bouncyButtonsEnabled) {
        return@composed this
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .pointerInput(key1 = buttonState) {
                    awaitPointerEventScope {
                        buttonState = if (buttonState == ButtonState.Pressed) {
                            waitForUpOrCancellation()
                            ButtonState.Idle
                        }
                        else {
                            awaitFirstDown(requireUnconsumed = false)
                            ButtonState.Pressed
                        }
                    }
                }
    }
    else {
        return@composed this
    }
}

/**
 * Animates the visibility of a composable with a fade and vertical offset animation.
 *
 * @param visible Determines whether the composable should be visible (true) or invisible (false).
 * @param index An optional index used to stagger the animation start for multiple items.
 * @param invisibleOffsetY The vertical offset in pixels applied when the composable is invisible. Defaults to 50.
 * @param animationDuration The duration of the animation in milliseconds. Defaults to 300.
 * @param staggerDelay The delay in milliseconds added for each item based on its index, used for staggered animations. Defaults to 64.
 *
 * @return A [Modifier] that applies the visibility animation to the composable.
 *
 * The animation consists of two parts:
 * 1. A fade in/out animation controlled by the [alpha] state.
 * 2. A vertical offset animation controlled by the [offsetYState] state.
 *
 * When [visible] is true, the composable fades in and the vertical offset is 0.
 * When [visible] is false, the composable fades out and the vertical offset is [invisibleOffsetY].
 *
 * The animation is staggered using the [staggerDelay] and [index] parameters,
 * such that each item starts its animation after a delay proportional to its index.
 *
 * The modifier also adds a vertical padding of 4.dp for visual spacing.
 */
fun Modifier.animateVisibility(
    visible : Boolean = true , index : Int = 0 , invisibleOffsetY : Int = 50 , animationDuration : Int = 300 , staggerDelay : Int = 64
) = composed {
    val alpha : State<Float> = animateFloatAsState(
        targetValue = if (visible) 1f else 0f , animationSpec = tween(
            durationMillis = animationDuration , delayMillis = index * staggerDelay
        ) , label = "Alpha"
    )

    val offsetState : State<Float> = animateFloatAsState(
        targetValue = if (visible) 0f else invisibleOffsetY.toFloat() , animationSpec = tween(
            durationMillis = animationDuration , delayMillis = index * staggerDelay
        ) , label = "OffsetY"
    )

    this
            .offset {
                IntOffset(x = 0 , y = offsetState.value.toInt())
            }
            .graphicsLayer {
                this.alpha = alpha.value
            }
}