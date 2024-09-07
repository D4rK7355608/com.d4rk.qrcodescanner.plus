package com.d4rk.qrcodescanner.plus.ui.settings.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.model.SearchEngine
import com.d4rk.qrcodescanner.plus.usecase.Settings
import com.d4rk.qrcodescanner.plus.utils.compose.components.RadioButtonPreferenceItem

@Composable
fun ChooseSearchEngineScreen(settings : Settings) {
    val selectedSearchEngine : MutableState<SearchEngine> =
            remember { mutableStateOf(settings.searchEngine) }

    LazyColumn(
        modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
    ) {
        items(SearchEngine.entries) { searchEngine ->
            val isChecked : Boolean = selectedSearchEngine.value == searchEngine
            RadioButtonPreferenceItem(text = stringResource(id = searchEngine.stringResId()) ,
                                      isChecked = isChecked ,
                                      onCheckedChange = { newIsChecked ->
                                          if (newIsChecked) {
                                              selectedSearchEngine.value = searchEngine
                                              settings.searchEngine = searchEngine
                                          }
                                      })
        }
    }
}

fun SearchEngine.stringResId() : Int {
    return when (this.name) {
        SearchEngine.NONE.name -> R.string.none
        SearchEngine.ASK_EVERY_TIME.name -> R.string.ask_every_time
        SearchEngine.STARTPAGE.name -> R.string.activity_choose_search_engine_startpage
        SearchEngine.BING.name -> R.string.activity_choose_search_engine_bing
        SearchEngine.DUCK_DUCK_GO.name -> R.string.activity_choose_search_engine_duck_duck_go
        SearchEngine.GOOGLE.name -> R.string.activity_choose_search_engine_google
        SearchEngine.QWANT.name -> R.string.activity_choose_search_engine_qwant
        SearchEngine.YAHOO.name -> R.string.activity_choose_search_engine_yahoo
        SearchEngine.YANDEX.name -> R.string.activity_choose_search_engine_yandex
        else -> throw IllegalArgumentException("Unsupported SearchEngine: ${this.name}")
    }
}