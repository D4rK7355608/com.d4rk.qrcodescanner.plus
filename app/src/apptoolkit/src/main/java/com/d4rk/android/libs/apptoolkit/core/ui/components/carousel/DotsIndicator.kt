package com.d4rk.android.libs.apptoolkit.core.ui.components.carousel

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraTinyHorizontalSpacer

@Composable
fun DotsIndicator(
    modifier : Modifier = Modifier , totalDots : Int , selectedIndex : Int , selectedColor : Color = MaterialTheme.colorScheme.primary , unSelectedColor : Color = Color.Gray , dotSize : Dp , animationDuration : Int = 300
) {
    val transition : Transition<Int> = updateTransition(targetState = selectedIndex , label = "Dot Transition")

    LazyRow(
        modifier = modifier
                .wrapContentWidth()
                .height(dotSize) , verticalAlignment = Alignment.CenterVertically
    ) {
        items(count = totalDots , key = { index -> index }) { index ->
            val animatedDotSize : Dp by transition.animateDp(transitionSpec = {
                tween(durationMillis = animationDuration , easing = FastOutSlowInEasing)
            } , label = "Dot Size Animation") {
                if (it == index) dotSize else dotSize / 1.4f
            }

            val isSelected : Boolean = index == selectedIndex
            val size : Dp = if (isSelected) animatedDotSize else animatedDotSize

            IndicatorDot(
                color = if (isSelected) selectedColor else unSelectedColor , size = size
            )

            if (index != totalDots - 1) {
                ExtraTinyHorizontalSpacer()
            }
        }
    }
}