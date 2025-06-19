package com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.chip.CommonFilterChip
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallHorizontalSpacer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopListFilters(modifier : Modifier = Modifier, filters : List<String> , selectedFilter : String , onFilterSelected : (String) -> Unit , label : String = stringResource(id = R.string.sort_by)) {
    val listState : LazyListState = rememberLazyListState()
    val scope : CoroutineScope = rememberCoroutineScope()
    Row(modifier = modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically) {
        LargeHorizontalSpacer()
        Text(text = label , style = MaterialTheme.typography.bodyMedium , fontWeight = FontWeight.Bold)
        LargeHorizontalSpacer()
        LazyRow(
            modifier = Modifier.weight(1f) , state = listState
        ) {
            items(filters.size) { index : Int ->
                val filter : String = filters[index]
                CommonFilterChip(selected = selectedFilter == filter , onClick = {
                    onFilterSelected(filter)
                    scope.launch { listState.animateScrollToItem(index) }
                } , label = filter)
                if (index < filters.size - 1) {
                    SmallHorizontalSpacer()
                }
            }
        }
    }
}