package com.d4rk.android.libs.apptoolkit.app.about.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.actions.AboutEvents
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.ui.UiAboutScreen
import com.d4rk.android.libs.apptoolkit.app.licenses.LicensesActivity
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.PreferenceCategoryItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.SettingsPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.snackbar.DefaultSnackbarHandler
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraTinyVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ClipboardHelper
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AboutSettingsList(paddingValues : PaddingValues = PaddingValues() , deviceProvider : AboutSettingsProvider , configProvider : BuildInfoProvider , snackbarHostState : SnackbarHostState) {
    val context : Context = LocalContext.current
    val viewModel : AboutViewModel = koinViewModel()
    val screenState : UiStateScreen<UiAboutScreen> by viewModel.uiState.collectAsState()
    val deviceInfo : String = stringResource(id = R.string.device_info)

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(event = AboutEvents.LoadHtml(context = context , packageName = configProvider.packageName , versionName = configProvider.appVersion))
    }

    LazyColumn(modifier = Modifier.fillMaxHeight() , contentPadding = paddingValues) {
        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.app_info))
            SmallVerticalSpacer()
            Column(
                modifier = Modifier
                        .padding(horizontal = SizeConstants.LargeSize)
                        .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            ) {
                SettingsPreferenceItem(title = stringResource(id = R.string.app_full_name) , summary = stringResource(id = R.string.copyright))
                ExtraTinyVerticalSpacer()
                SettingsPreferenceItem(title = stringResource(id = R.string.app_build_version) , summary = configProvider.appVersion + " (${configProvider.appVersionCode})")
                ExtraTinyVerticalSpacer()
                SettingsPreferenceItem(title = stringResource(id = R.string.oss_license_title) , summary = stringResource(id = R.string.summary_preference_settings_oss)) {
                    IntentsHelper.openActivity(context = context, activityClass = LicensesActivity::class.java)
                }
            }
        }

        item {
            PreferenceCategoryItem(title = deviceInfo)
            SmallVerticalSpacer()
            Column(
                modifier = Modifier
                        .padding(horizontal = SizeConstants.LargeSize)
                        .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            ) {
                SettingsPreferenceItem(title = deviceInfo , summary = deviceProvider.deviceInfo) {
                    ClipboardHelper.copyTextToClipboard(
                        context = context , label = deviceInfo , text = deviceProvider.deviceInfo , onShowSnackbar = {
                            viewModel.onEvent(event = AboutEvents.CopyDeviceInfo)
                        })
                }
            }
        }
    }

    DefaultSnackbarHandler(screenState = screenState , snackbarHostState = snackbarHostState , getDismissEvent = { AboutEvents.DismissSnackbar } , onEvent = { viewModel.onEvent(it) })
}