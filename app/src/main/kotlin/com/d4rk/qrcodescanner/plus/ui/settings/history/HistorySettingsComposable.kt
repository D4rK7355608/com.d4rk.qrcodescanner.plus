package com.d4rk.qrcodescanner.plus.ui.settings.history

import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.d4rk.qrcodescanner.plus.utils.compose.components.PreferenceItem
import com.d4rk.qrcodescanner.plus.utils.compose.components.SwitchPreferenceItem
import com.d4rk.qrcodescanner.plus.utils.haptic.weakHapticFeedback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorySettingsComposable(activity : HistorySettingsActivity) {
    val context : Context = LocalContext.current
    val view : View = LocalView.current
    val dataStore : DataStore = DataStore.getInstance(context)
    val scrollBehavior : TopAppBarScrollBehavior =
            TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val saveScannedBarcodesToHistory : Boolean by dataStore.saveScannedBarcodesToHistory.collectAsState(
        initial = true
    )
    val saveCreatedBarcodesToHistory : Boolean by dataStore.saveCreatedBarcodesToHistory.collectAsState(
        initial = true
    )
    val doNotSaveDuplicates : Boolean by dataStore.doNotSaveDuplicates.collectAsState(initial = false)

    val scope : CoroutineScope = rememberCoroutineScope()

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) , topBar = {
        LargeTopAppBar(title = { Text(stringResource(R.string.history)) } , navigationIcon = {
            IconButton(onClick = {
                view.weakHapticFeedback()
                activity.finish()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack , contentDescription = null)
            }
        } , scrollBehavior = scrollBehavior)
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                    .fillMaxHeight()
                    .padding(paddingValues) ,
        ) {

            item {
                SwitchPreferenceItem(
                    title = "Keep scanned scans" ,
                    summary = stringResource(R.string.save_scanned_barcodes_to_history) ,
                    checked = saveScannedBarcodesToHistory
                ) { isChecked ->
                    scope.launch {
                        dataStore.saveSaveScannedBarcodesToHistory(isChecked)
                    }
                }
            }

            item {
                SwitchPreferenceItem(
                    title = "Keep created scans" ,
                    summary = stringResource(R.string.save_created_barcodes_to_history) ,
                    checked = saveCreatedBarcodesToHistory
                ) { isChecked ->
                    scope.launch {
                        dataStore.saveSaveCreatedBarcodesToHistory(isChecked)
                    }
                }
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.do_not_save_duplicates) ,
                    summary = "Prevent duplicate history entries." ,
                    checked = doNotSaveDuplicates
                ) { isChecked ->
                    scope.launch {
                        dataStore.saveDoNotSaveDuplicates(isChecked)
                    }
                }
            }

            item {
                PreferenceItem(
                    title = stringResource(R.string.clear_history)
                ) {
                    view.weakHapticFeedback()
                    // TODO: Add action
                }
            }
        }
    }
}