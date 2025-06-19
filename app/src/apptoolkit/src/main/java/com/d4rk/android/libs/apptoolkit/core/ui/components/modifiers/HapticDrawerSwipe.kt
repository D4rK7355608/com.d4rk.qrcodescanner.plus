package com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

/**
 * A modifier that adds haptic feedback when a drawer is swiped open or closed.
 *
 * This modifier uses the [DrawerState] to detect when a drawer is being opened or closed via swipe
 * gestures, and triggers a haptic feedback (long press) when the drawer animation starts.
 * The haptic feedback is only triggered once per animation, and is reset when the animation completes.
 *
 * @param state The [DrawerState] of the drawer to monitor for swipe events.
 * @return A [Modifier] that applies the haptic feedback behavior.
 *
 * Example Usage:
 * ```
 * ModalNavigationDrawer(
 *      state = state,
 *      drawerContent = { ... },
 *      modifier = Modifier.hapticDrawerSwipe(state)
 *  ) {
 *      // Content
 *  }
 * ```
 */
fun Modifier.hapticDrawerSwipe(state : DrawerState) : Modifier = composed {
    val feedback : HapticFeedback = LocalHapticFeedback.current
    var hasFeedbackTriggered : Boolean by remember { mutableStateOf(value = false) }

    LaunchedEffect(key1 = state.currentValue , key2 = state.targetValue) {
        if (state.isAnimationRunning && ! hasFeedbackTriggered) {
            feedback.performHapticFeedback(HapticFeedbackType.LongPress)
            hasFeedbackTriggered = true
        }

        if (! state.isAnimationRunning) {
            hasFeedbackTriggered = false
        }
    }

    return@composed this
}