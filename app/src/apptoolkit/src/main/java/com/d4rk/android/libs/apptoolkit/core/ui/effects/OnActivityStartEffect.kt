package com.d4rk.android.libs.apptoolkit.core.ui.effects

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle

@Composable
fun OnActivityStartEffect(onStart: () -> Unit) {
    ActivityLifecycleEffect(lifecycleEvent = Lifecycle.Event.ON_START, onEvent = onStart)
}
