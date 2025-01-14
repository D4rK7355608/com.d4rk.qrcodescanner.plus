package com.d4rk.qrcodescanner.plus.ui.screens.settings.scanner

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
import com.d4rk.qrcodescanner.plus.ui.screens.settings.scanner.camera.ChooseCameraActivity
import com.d4rk.qrcodescanner.plus.ui.screens.settings.scanner.formats.SupportedFormatsActivity
import com.d4rk.qrcodescanner.plus.utils.IntentUtils
import com.d4rk.qrcodescanner.plus.utils.compose.components.PreferenceItem
import com.d4rk.qrcodescanner.plus.utils.compose.components.SwitchPreferenceItem
import com.d4rk.qrcodescanner.plus.utils.haptic.weakHapticFeedback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerSettingsComposable(activity: ScannerSettingsActivity) {
    val context: Context = LocalContext.current
    val view: View = LocalView.current
    val dataStore: DataStore = DataStore.getInstance(context)
    val scope: CoroutineScope = rememberCoroutineScope()

    val scrollBehavior: TopAppBarScrollBehavior =
            TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val openLinksAutomatically : Boolean by dataStore.openLinksAutomatically.collectAsState(initial = false)
    val copyToClipboard : Boolean by dataStore.copyToClipboard.collectAsState(initial = true)
    val simpleAutoFocus : Boolean by dataStore.simpleAutoFocus.collectAsState(initial = false)
    val flash : Boolean by dataStore.flash.collectAsState(initial = false)
    val vibrate : Boolean by dataStore.vibrate.collectAsState(initial = true)
    val continuousScanning : Boolean by dataStore.continuousScanning.collectAsState(initial = false)
    val confirmScansManually : Boolean by dataStore.confirmScansManually.collectAsState(initial = false)

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        LargeTopAppBar(title = { Text(stringResource(R.string.scanner)) }, navigationIcon = {
            IconButton(onClick = {
                view.weakHapticFeedback()
                activity.finish()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        }, scrollBehavior = scrollBehavior)
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                    .fillMaxHeight()
                    .padding(paddingValues)
        ) {

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.open_content_automatically),
                    summary = stringResource(R.string.summary_preference_settings_open_content_automatically),
                    checked = openLinksAutomatically
                ) { isChecked ->
                    scope.launch {
                        dataStore.saveOpenLinksAutomatically(isChecked)
                    }
                }
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.copy_to_clipboard),
                    summary = stringResource(R.string.summary_preference_settings_copy_to_clipboard),
                    checked = copyToClipboard
                ) { isChecked ->
                    scope.launch {
                        dataStore.saveCopyToClipboard(isChecked)
                    }
                }
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.simple_auto_focus),
                    summary = stringResource(R.string.summary_preference_settings_simple_auto_focus),
                    checked = simpleAutoFocus
                ) { isChecked ->
                    scope.launch {
                        dataStore.saveSimpleAutoFocus(isChecked)
                    }
                }
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.flash),
                    summary = stringResource(R.string.summary_preference_settings_enable_flash_on_start),
                    checked = flash
                ) { isChecked ->
                    scope.launch {
                        dataStore.saveFlash(isChecked)
                    }
                }
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.vibrate),
                    summary = stringResource(R.string.summary_preference_settings_vibrate),
                    checked = vibrate
                ) { isChecked ->
                    scope.launch {
                        dataStore.saveVibrate(isChecked)
                    }
                }
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.continuous_scanning),
                    summary = stringResource(R.string.summary_preference_settings_continuous_scanning),
                    checked = continuousScanning
                ) { isChecked ->
                    scope.launch {
                        dataStore.saveContinuousScanning(isChecked)
                    }
                }
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.confirm_scans_manually),
                    summary = stringResource(R.string.summary_preference_settings_confirm_scans_manually),
                    checked = confirmScansManually
                ) { isChecked ->
                    scope.launch {
                        dataStore.saveConfirmScansManually(isChecked)
                    }
                }
            }

            item {
                PreferenceItem(
                    title = stringResource(R.string.camera)
                ) {
                    view.weakHapticFeedback()
                    IntentUtils.openActivity(context , _root_ide_package_.com.d4rk.qrcodescanner.plus.ui.screens.settings.scanner.camera.ChooseCameraActivity::class.java)
                }
            }

            item {
                PreferenceItem(
                    title = stringResource(R.string.supported_formats)
                ) {
                    view.weakHapticFeedback()
                    IntentUtils.openActivity(context, SupportedFormatsActivity::class.java)
                }
            }
        }
    }
}