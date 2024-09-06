package com.d4rk.qrcodescanner.plus.ui.settings.scanner

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.ui.settings.camera.ChooseCameraActivity
import com.d4rk.qrcodescanner.plus.ui.settings.formats.SupportedFormatsActivity
import com.d4rk.qrcodescanner.plus.usecase.Settings
import com.d4rk.qrcodescanner.plus.utils.IntentUtils
import com.d4rk.qrcodescanner.plus.utils.compose.components.PreferenceItem
import com.d4rk.qrcodescanner.plus.utils.compose.components.SwitchPreferenceItem
import com.d4rk.qrcodescanner.plus.utils.haptic.weakHapticFeedback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerSettingsComposable(activity : ScannerSettingsActivity) {
    val context : Context = LocalContext.current
    val view : View = LocalView.current
    val scrollBehavior : TopAppBarScrollBehavior =
            TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val settings : Settings = remember { Settings.getInstance(context) }

    val openLinksAutomatically : MutableState<Boolean> =
            remember { mutableStateOf(value = settings.openLinksAutomatically) }
    val copyToClipboard : MutableState<Boolean> =
            remember { mutableStateOf(value = settings.copyToClipboard) }
    val simpleAutoFocus : MutableState<Boolean> =
            remember { mutableStateOf(value = settings.simpleAutoFocus) }
    val flash : MutableState<Boolean> = remember { mutableStateOf(value = settings.flash) }
    val vibrate : MutableState<Boolean> = remember { mutableStateOf(value = settings.vibrate) }
    val continuousScanning : MutableState<Boolean> =
            remember { mutableStateOf(value = settings.continuousScanning) }
    val confirmScansManually : MutableState<Boolean> =
            remember { mutableStateOf(value = settings.confirmScansManually) }

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) , topBar = {
        LargeTopAppBar(title = { Text(stringResource(R.string.display)) } , navigationIcon = {
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
                    title = stringResource(R.string.open_content_automatically) ,
                    summary = stringResource(R.string.summary_preference_settings_open_content_automatically) ,
                    checked = settings.openLinksAutomatically
                ) { isChecked ->
                    openLinksAutomatically.value = isChecked
                    settings.openLinksAutomatically = isChecked
                }
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.copy_to_clipboard) ,
                    summary = stringResource(R.string.summary_preference_settings_copy_to_clipboard) ,
                    checked = settings.copyToClipboard
                ) { isChecked ->
                    copyToClipboard.value = isChecked
                    settings.copyToClipboard = isChecked
                }
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.simple_auto_focus) ,
                    summary = stringResource(R.string.summary_preference_settings_simple_auto_focus) ,
                    checked = settings.simpleAutoFocus
                ) { isChecked ->
                    simpleAutoFocus.value = isChecked
                    settings.simpleAutoFocus = isChecked
                }
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.flash) ,
                    summary = stringResource(R.string.summary_preference_settings_enable_flash_on_start) ,
                    checked = settings.flash
                ) { isChecked ->
                    flash.value = isChecked
                    settings.flash = isChecked
                }
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.vibrate) ,
                    summary = stringResource(R.string.summary_preference_settings_vibrate) ,
                    checked = settings.vibrate
                ) { isChecked ->
                    vibrate.value = isChecked
                    settings.vibrate = isChecked
                }
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.continuous_scanning) ,
                    summary = stringResource(R.string.summary_preference_settings_continuous_scanning) ,
                    checked = settings.continuousScanning
                ) { isChecked ->
                    continuousScanning.value = isChecked
                    settings.continuousScanning = isChecked
                }
            }

            item {
                SwitchPreferenceItem(
                    title = stringResource(R.string.confirm_scans_manually) ,
                    summary = stringResource(R.string.summary_preference_settings_confirm_scans_manually) ,
                    checked = settings.confirmScansManually
                ) { isChecked ->
                    confirmScansManually.value = isChecked
                    settings.confirmScansManually = isChecked
                }
            }

            item {
                PreferenceItem(
                    title = stringResource(R.string.camera)
                ) {
                    view.weakHapticFeedback()
                    IntentUtils.openActivity(context , ChooseCameraActivity::class.java)
                }
            }

            item {
                PreferenceItem(
                    title = stringResource(R.string.supported_formats)
                ) {
                    view.weakHapticFeedback()
                    IntentUtils.openActivity(context , SupportedFormatsActivity::class.java)
                }
            }
        }
    }
}