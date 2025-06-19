package com.d4rk.android.libs.apptoolkit.app.oboarding.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageIndicatorDots(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = pagerState.currentPage == index
            val animatedScaleFactor by animateFloatAsState(
                targetValue = if (isSelected) 1.4f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "DotScaleFactor"
            )
            val color = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            val baseDotSize = SizeConstants.SmallSize

            Box(
                modifier = Modifier
                        .animateContentSize()
                        .size(baseDotSize * animatedScaleFactor)
                        .background(color, RoundedCornerShape(percent = 50))
            )
        }
    }
}