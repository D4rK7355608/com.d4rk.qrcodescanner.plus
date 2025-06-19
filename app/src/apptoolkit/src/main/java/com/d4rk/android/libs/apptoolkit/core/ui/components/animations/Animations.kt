package com.d4rk.android.libs.apptoolkit.core.ui.components.animations

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.delay

@Composable
fun rememberAnimatedVisibilityState(listState : LazyListState , itemCount : Int) : Pair<SnapshotStateList<Boolean> , MutableState<Boolean>> {
    val visibilityStates : SnapshotStateList<Boolean> = remember { mutableStateListOf() }
    val isFabVisible : MutableState<Boolean> = remember { mutableStateOf(value = false) }
    var initialAnimationPlayed : Boolean by remember { mutableStateOf(value = false) }

    LaunchedEffect(key1 = itemCount) {
        visibilityStates.clear()
        visibilityStates.addAll(List(size = itemCount) { false })
        if (itemCount == 0) {
            isFabVisible.value = true
        }
    }

    LaunchedEffect(Unit) {
        val firstVisible : Int = listState.firstVisibleItemIndex
        val lastVisible : Int = (firstVisible + listState.layoutInfo.visibleItemsInfo.size - 1).coerceAtMost(maximumValue = itemCount - 1)
        (firstVisible..lastVisible).forEach { index : Int ->
            delay(timeMillis = index * 8L)
            if (index < visibilityStates.size) {
                visibilityStates[index] = true
            }
        }
        initialAnimationPlayed = true
        delay(timeMillis = 50L)
        isFabVisible.value = true
    }

    if (initialAnimationPlayed) {
        visibilityStates.forEachIndexed { index : Int , isVisible : Boolean ->
            if (! isVisible && index < visibilityStates.size) {
                visibilityStates[index] = true
            }
        }
    }

    return visibilityStates to isFabVisible
}

@Composable
fun rememberAnimatedVisibilityStateForGrids(gridState : LazyGridState , itemCount : Int) : Pair<SnapshotStateList<Boolean> , MutableState<Boolean>> {
    val visibilityStates : SnapshotStateList<Boolean> = remember { mutableStateListOf() }
    val isFabVisible : MutableState<Boolean> = remember { mutableStateOf(value = false) }
    var initialAnimationPlayed : Boolean by remember { mutableStateOf(value = false) }

    LaunchedEffect(key1 = itemCount) {
        visibilityStates.clear()
        visibilityStates.addAll(List(size = itemCount) { false })
        if (itemCount == 0) {
            isFabVisible.value = true
        }
    }

    LaunchedEffect(Unit) {
        val firstVisible : Int = gridState.firstVisibleItemIndex
        val lastVisible : Int = (firstVisible + gridState.layoutInfo.visibleItemsInfo.size - 1).coerceAtMost(maximumValue = itemCount - 1)
        (firstVisible..lastVisible).forEach { index : Int ->
            delay(timeMillis = index * 8L)
            if (index < visibilityStates.size) {
                visibilityStates[index] = true
            }
        }
        initialAnimationPlayed = true
        delay(timeMillis = 50L)
        isFabVisible.value = true
    }

    if (initialAnimationPlayed) {
        visibilityStates.forEachIndexed { index : Int , isVisible : Boolean ->
            if (! isVisible && index < visibilityStates.size) {
                visibilityStates[index] = true
            }
        }
    }

    return visibilityStates to isFabVisible
}