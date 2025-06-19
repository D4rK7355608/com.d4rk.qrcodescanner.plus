package com.d4rk.android.libs.apptoolkit.app.help.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.UiHelpQuestion
import com.d4rk.android.libs.apptoolkit.core.ui.components.animations.rememberAnimatedVisibilityState
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.animateVisibility

@Composable
fun HelpQuestionsList(questions : List<UiHelpQuestion>) {
    val expandedStates : SnapshotStateMap<Int , Boolean> = remember { mutableStateMapOf() }
    val listState : LazyListState = rememberLazyListState()

    val (visibilityStates : SnapshotStateList<Boolean>) = rememberAnimatedVisibilityState(listState = listState , itemCount = questions.size)

    Column {
        questions.forEachIndexed { index : Int , question : UiHelpQuestion ->
            val isExpanded = expandedStates[index] == true
            QuestionCard(title = question.question , summary = question.answer , isExpanded = isExpanded , onToggleExpand = {
                expandedStates[index] = ! isExpanded
            } , modifier = Modifier.animateVisibility(visible = visibilityStates.getOrElse(index = index) { false } , index = index))
        }
    }
}