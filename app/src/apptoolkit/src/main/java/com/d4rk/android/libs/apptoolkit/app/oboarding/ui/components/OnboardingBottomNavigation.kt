package com.d4rk.android.libs.apptoolkit.app.oboarding.ui.components

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingBottomNavigation(
    pagerState : PagerState , pageCount : Int , onNextClicked : () -> Unit , onBackClicked : () -> Unit
) {
    val view : View = LocalView.current

    BottomAppBar {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SizeConstants.LargeSize, vertical = SizeConstants.SmallSize) , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f)) {
                this@Row.AnimatedVisibility(visible = pagerState.currentPage > 0 , modifier = Modifier.fillMaxWidth() , enter = slideInHorizontally(initialOffsetX = { - it } , animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy , stiffness = Spring.StiffnessLow
                )) + fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) , exit = slideOutHorizontally(targetOffsetX = { - it } , animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy , stiffness = Spring.StiffnessLow
                )) + fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))) {

                    Box(contentAlignment = Alignment.CenterStart) {
                        OutlinedButton(
                            onClick = {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                onBackClicked()
                                      } , modifier = Modifier.bounceClick()
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft , contentDescription = stringResource(id = R.string.back_button_content_description) , modifier = Modifier.size(SizeConstants.ButtonIconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = stringResource(id = R.string.back_button_text), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }

            PageIndicatorDots(pagerState = pagerState , pageCount = pageCount , modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize() , contentAlignment = Alignment.CenterEnd
            ) {
                val isLastPage = pagerState.currentPage == pageCount - 1
                Button(
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onNextClicked()
                              } , modifier = Modifier
                        .animateContentSize()
                        .bounceClick()
                ) {
                    if (isLastPage) {
                        Icon(
                            imageVector = Icons.Filled.Check , contentDescription = stringResource(id = R.string.done_button_content_description) , modifier = Modifier.size(SizeConstants.ButtonIconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = stringResource(id = R.string.done_button_text), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    else {
                        Text(text = stringResource(id = R.string.next_button_text), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight , contentDescription = stringResource(id = R.string.next_button_content_description) , modifier = Modifier.size(SizeConstants.ButtonIconSize)
                        )
                    }
                }
            }
        }
    }
}