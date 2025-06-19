package com.d4rk.android.libs.apptoolkit.core.ui.components.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun IndicatorDot(
    modifier : Modifier = Modifier ,
    size : Dp ,
    color : Color ,
) {
    Box(
        modifier = modifier
                .size(size)
                .clip(CircleShape)
                .background(color)
    )
}