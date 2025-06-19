package com.d4rk.android.libs.apptoolkit.core.ui.effects

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle

@Composable
fun OnStopEffect(onStop: () -> Unit) {
    LifecycleEffect(lifecycleEvent = Lifecycle.Event.ON_STOP , onEvent = onStop)
}