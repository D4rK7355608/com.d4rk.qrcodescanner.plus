package com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers

import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
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
 * A modifier that adds haptic feedback to a `SwipeToDismissBox` when the swipe state changes.
 *
 * This modifier provides haptic feedback when the `SwipeToDismissBox` is being swiped and
 * is not in the settled state. It will trigger a long press haptic feedback once per swipe
 * gesture.  Once the state is settled, it will reset and be ready to give feedback on next swipe.
 *
 * @param swipeToDismissBoxState The state of the `SwipeToDismissBox`.
 * @return A `Modifier` that adds haptic feedback to the `SwipeToDismissBox`.
 *
 * Example Usage:
 * ```
 * val swipeState = rememberSwipeToDismissBoxState()
 * Box(
 *    modifier = Modifier
 *        .fillMaxWidth()
 *        .hapticSwipeToDismissBox(swipeToDismissBoxState = swipeState)
 */
fun Modifier.hapticSwipeToDismissBox(swipeToDismissBoxState : SwipeToDismissBoxState) : Modifier = composed {
    val haptic : HapticFeedback = LocalHapticFeedback.current
    var hasVibrated : Boolean by remember { mutableStateOf(value = false) }

    LaunchedEffect(swipeToDismissBoxState.currentValue) {
        if (swipeToDismissBoxState.currentValue != SwipeToDismissBoxValue.Settled && ! hasVibrated) {
            haptic.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.LongPress)
            hasVibrated = true
        }
        else if (swipeToDismissBoxState.currentValue == SwipeToDismissBoxValue.Settled) {
            hasVibrated = false
        }
    }

    return@composed this
}