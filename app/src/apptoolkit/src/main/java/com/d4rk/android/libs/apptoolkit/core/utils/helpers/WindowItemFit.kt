package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object WindowItemFit {
    @Composable
    fun count(itemHeight : Dp , itemSpacing : Dp = 0.dp , paddingValues : PaddingValues = PaddingValues(0.dp)) : Int {
        val windowInfo = LocalWindowInfo.current
        val density = LocalDensity.current
        val containerHeightPx = windowInfo.containerSize.height.toFloat()

        val contentPaddingPx = with(density) {
            (paddingValues.calculateTopPadding() + paddingValues.calculateBottomPadding()).toPx()
        }
        val itemHeightPx = with(density) { itemHeight.toPx() }
        val itemSpacingPx = with(density) { itemSpacing.toPx() }
        val availableHeightPx = containerHeightPx - contentPaddingPx

        return ((availableHeightPx + itemSpacingPx) / (itemHeightPx + itemSpacingPx)).toInt().coerceAtLeast(1) + 4
    }
}