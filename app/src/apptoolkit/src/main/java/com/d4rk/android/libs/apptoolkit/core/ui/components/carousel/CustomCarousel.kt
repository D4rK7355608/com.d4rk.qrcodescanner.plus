package com.d4rk.android.libs.apptoolkit.core.ui.components.carousel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.hapticPagerSwipe
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlin.math.absoluteValue

@Composable
fun <T> CustomCarousel(
    items : List<T> , sidePadding : Dp , pagerState : PagerState , itemContent : @Composable (item : T) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState ,
            modifier = Modifier
                    .fillMaxWidth()
                    .hapticPagerSwipe(pagerState = pagerState) ,
            contentPadding = PaddingValues(horizontal = sidePadding) ,
        ) { page ->
            val pageOffset = remember(pagerState.currentPage , page) {
                (pagerState.currentPage - page).absoluteValue.toFloat()
            }
            CarouselItem(item = items[page] , pageOffset = pageOffset , itemContent = itemContent)
        }

        LargeVerticalSpacer()

        DotsIndicator(
            modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(bottom = SizeConstants.SmallSize) ,
            totalDots = items.size ,
            selectedIndex = pagerState.currentPage ,
            dotSize = SizeConstants.MediumSize / 2 ,
        )
    }
}