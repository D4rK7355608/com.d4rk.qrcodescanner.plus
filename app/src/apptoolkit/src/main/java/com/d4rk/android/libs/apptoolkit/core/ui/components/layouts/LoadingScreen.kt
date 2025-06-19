package com.d4rk.android.libs.apptoolkit.core.ui.components.layouts

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
                .fillMaxSize()
                .animateContentSize() , contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}