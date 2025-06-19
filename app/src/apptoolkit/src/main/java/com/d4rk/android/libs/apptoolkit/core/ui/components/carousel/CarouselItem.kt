package com.d4rk.android.libs.apptoolkit.core.ui.components.carousel

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp

@Composable
fun <T> CarouselItem(
    item : T , pageOffset : Float , itemContent : @Composable (item : T) -> Unit
) {
    val scale = animateFloatAsState(
        targetValue = lerp(0.95f , 1f , 1f - pageOffset.coerceIn(0f , 1f)) , animationSpec = tween(250) , label = "Carousel Item Scale for Page $pageOffset"
    ).value

    val alpha = lerp(0.5f , 1f , 1f - pageOffset.coerceIn(0f , 1f))

    Card(modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }) {
        itemContent(item)
    }
}