package com.d4rk.android.libs.apptoolkit.core.ui.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun LifecycleEffect(lifecycleEvent : Lifecycle.Event , onEvent : () -> Unit) {
    val lifecycleOwner : LifecycleOwner = LocalLifecycleOwner.current
    val latestOnEvent by rememberUpdatedState(newValue = onEvent)

    DisposableEffect(key1 = lifecycleOwner , key2 = lifecycleEvent) {
        val observer = LifecycleEventObserver { _ , event ->
            if (event == lifecycleEvent) {
                latestOnEvent()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}