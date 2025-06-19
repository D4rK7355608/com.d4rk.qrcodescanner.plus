package com.d4rk.android.libs.apptoolkit.core.ui.effects

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle

@Composable
fun OnPauseEffect(onPause: () -> Unit) {
    LifecycleEffect(lifecycleEvent = Lifecycle.Event.ON_PAUSE , onEvent = onPause)
}