package com.d4rk.android.libs.apptoolkit.core.ui.effects

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle

@Composable
fun OnResumeEffect(onResume: () -> Unit) {
    LifecycleEffect(lifecycleEvent = Lifecycle.Event.ON_RESUME, onEvent = onResume)
}