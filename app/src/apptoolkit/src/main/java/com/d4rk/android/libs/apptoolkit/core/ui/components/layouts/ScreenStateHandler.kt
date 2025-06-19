package com.d4rk.android.libs.apptoolkit.core.ui.components.layouts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import kotlinx.coroutines.delay

@Composable
fun <T> ScreenStateHandler(screenState : UiStateScreen<T> , onLoading : @Composable () -> Unit , onEmpty : @Composable () -> Unit , onSuccess : @Composable (T) -> Unit , onError : (@Composable () -> Unit)? = null) {
    var currentScreenState : ScreenState by remember { mutableStateOf(value = screenState.screenState) }

    LaunchedEffect(screenState.screenState) {
        if (screenState.screenState is ScreenState.IsLoading) {
            delay(timeMillis = 2500)
        }
        currentScreenState = screenState.screenState
    }
    val animationSpec : ContentTransform = remember {
        fadeIn(animationSpec = tween(durationMillis = 300)) togetherWith fadeOut(animationSpec = tween(durationMillis = 300))
    }

    AnimatedContent(targetState = currentScreenState , transitionSpec = { animationSpec } , label = "ScreenStateTransition") { currentState ->
        when (currentState) {
            is ScreenState.IsLoading -> {
                onLoading()
            }

            is ScreenState.NoData -> {
                onEmpty()
            }

            is ScreenState.Success -> {
                screenState.data?.let {
                    onSuccess(it)
                }
            }

            is ScreenState.Error -> {
                onError?.invoke()
            }
        }
    }
}