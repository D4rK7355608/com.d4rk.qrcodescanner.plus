package com.d4rk.android.libs.apptoolkit.core.ui.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.activity.ComponentActivity
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.findActivity

@Composable
fun ActivityLifecycleEffect(lifecycleEvent: Lifecycle.Event, onEvent: () -> Unit) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val latestOnEvent by rememberUpdatedState(newValue = onEvent)

    if (activity != null) {
        DisposableEffect(activity, lifecycleEvent) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == lifecycleEvent) {
                    latestOnEvent()
                }
            }
            activity.lifecycle.addObserver(observer)
            onDispose {
                activity.lifecycle.removeObserver(observer)
            }
        }
    }
}
