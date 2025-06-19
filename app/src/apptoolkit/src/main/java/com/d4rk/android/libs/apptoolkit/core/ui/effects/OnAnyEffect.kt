package com.d4rk.android.libs.apptoolkit.core.ui.effects

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle

@Composable
fun OnAnyEffect(onAny: () -> Unit) {
    LifecycleEffect(lifecycleEvent = Lifecycle.Event.ON_ANY , onEvent = onAny)
}