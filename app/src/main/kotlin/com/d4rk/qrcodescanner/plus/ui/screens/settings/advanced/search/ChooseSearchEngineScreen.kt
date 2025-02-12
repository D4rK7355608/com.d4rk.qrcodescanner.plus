package com.d4rk.qrcodescanner.plus.ui.screens.settings.advanced.search

import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.data.datastore.DataStore
import com.d4rk.qrcodescanner.plus.model.SearchEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseSearchEngineScreen(activity : ChooseSearchEngineActivity) {
    val context : Context = LocalContext.current
    val dataStore : DataStore = DataStore.getInstance(context)
    val scope : CoroutineScope = rememberCoroutineScope()
    val selectedSearchEngine : SearchEngine by dataStore.searchEngine.collectAsState(initial = SearchEngine.NONE)
    val view : View = LocalView.current
    val scrollBehavior : TopAppBarScrollBehavior =
            TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) , topBar = {
        LargeTopAppBar(title = { Text(stringResource(R.string.search_engines)) } , navigationIcon = {
            IconButton(onClick = {
                activity.finish()
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack , contentDescription = null
                )
            }
        } , scrollBehavior = scrollBehavior)
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                    .fillMaxHeight()
                    .padding(paddingValues) ,
        ) {
            items(SearchEngine.entries) { searchEngine ->
                val isChecked : Boolean = selectedSearchEngine == searchEngine
                /*RadioButtonPreferenceItem(
                    text = stringResource(id = searchEngine.stringResId()) , isChecked = isChecked
                ) { newIsChecked ->
                    if (newIsChecked) {
                        scope.launch {
                            dataStore.saveSearchEngine(searchEngine)
                        }
                    }
                }*/
            }
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