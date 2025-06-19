package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.components.ConsentSectionHeader
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.components.ConsentToggleCard
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.components.ExpandableConsentSectionHeader
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.SwitchCardItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ConsentManagerHelper
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun UsageAndDiagnosticsList(paddingValues: PaddingValues, configProvider: BuildInfoProvider) {
    val context: Context = LocalContext.current
    val dataStore: CommonDataStore = CommonDataStore.getInstance(context = context)
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val switchState: State<Boolean> =
        dataStore.usageAndDiagnostics(default = !configProvider.isDebugBuild)
            .collectAsState(initial = !configProvider.isDebugBuild)

    val analyticsState: State<Boolean> = dataStore.analyticsConsent(default = !configProvider.isDebugBuild)
        .collectAsState(initial = !configProvider.isDebugBuild)
    val adStorageState: State<Boolean> = dataStore.adStorageConsent(default = !configProvider.isDebugBuild)
        .collectAsState(initial = !configProvider.isDebugBuild)
    val adUserDataState: State<Boolean> = dataStore.adUserDataConsent(default = !configProvider.isDebugBuild)
        .collectAsState(initial = !configProvider.isDebugBuild)
    val adPersonalizationState: State<Boolean> = dataStore.adPersonalizationConsent(default = !configProvider.isDebugBuild)
        .collectAsState(initial = !configProvider.isDebugBuild)


    var advancedSettingsExpanded: Boolean by remember { mutableStateOf<Boolean>(value = false) }

    fun updateAllConsents() {
        ConsentManagerHelper.updateConsent(
            analyticsGranted = analyticsState.value,
            adStorageGranted = adStorageState.value,
            adUserDataGranted = adUserDataState.value,
            adPersonalizationGranted = adPersonalizationState.value
        )
    }

    LazyColumn(contentPadding = paddingValues, modifier = Modifier.fillMaxSize()) {
        item {
            SwitchCardItem(
                title = stringResource(id = R.string.usage_and_diagnostics),
                switchState = switchState
            ) { isChecked: Boolean ->
                coroutineScope.launch {
                    dataStore.saveUsageAndDiagnostics(isChecked = isChecked)

                }
            }
        }

        item {
            ExpandableConsentSectionHeader(
                title = stringResource(id = R.string.advanced_privacy_settings),
                expanded = advancedSettingsExpanded,
                onToggle = { advancedSettingsExpanded = !advancedSettingsExpanded })
        }

        item {
            AnimatedVisibility(
                visible = advancedSettingsExpanded,
                enter = expandVertically(expandFrom = Alignment.Top),
                exit = shrinkVertically(shrinkTowards = Alignment.Top),
            ) {
                Column(modifier = Modifier.padding(horizontal = SizeConstants.SmallSize)) {
                    ConsentSectionHeader(title = stringResource(id = R.string.consent_category_analytics_title))
                    ConsentToggleCard(
                        title = stringResource(id = R.string.consent_analytics_storage_title),
                        description = stringResource(id = R.string.consent_analytics_storage_description),
                        switchState = analyticsState.value,
                        icon = Icons.Outlined.Analytics,
                        onCheckedChange = { isChecked: Boolean ->
                            coroutineScope.launch {
                                dataStore.saveAnalyticsConsent(isGranted = isChecked)
                                updateAllConsents()
                            }
                        })

                    SmallVerticalSpacer()

                    ConsentSectionHeader(title = stringResource(id = R.string.consent_category_advertising_title))
                    ConsentToggleCard(
                        title = stringResource(id = R.string.consent_ad_storage_title),
                        description = stringResource(id = R.string.consent_ad_storage_description),
                        switchState = adStorageState.value,
                        icon = Icons.Outlined.Storage,
                        onCheckedChange = { isChecked: Boolean ->
                            coroutineScope.launch {
                                dataStore.saveAdStorageConsent(isGranted = isChecked)
                                updateAllConsents()
                            }
                        })

                    SmallVerticalSpacer()

                    ConsentToggleCard(
                        title = stringResource(id = R.string.consent_ad_user_data_title),
                        description = stringResource(id = R.string.consent_ad_user_data_description),
                        switchState = adUserDataState.value,
                        icon = Icons.AutoMirrored.Outlined.Send,
                        onCheckedChange = { isChecked: Boolean ->
                            coroutineScope.launch {
                                dataStore.saveAdUserDataConsent(isGranted = isChecked)
                                updateAllConsents()
                            }
                        })

                    SmallVerticalSpacer()

                    ConsentToggleCard(
                        title = stringResource(id = R.string.consent_ad_personalization_title),
                        description = stringResource(id = R.string.consent_ad_personalization_description),
                        switchState = adPersonalizationState.value,
                        icon = Icons.Outlined.Campaign,
                        onCheckedChange = { isChecked: Boolean ->
                            coroutineScope.launch {
                                dataStore.saveAdPersonalizationConsent(isGranted = isChecked)
                                updateAllConsents()
                            }
                        })
                }
            }
        }

        item {
            InfoMessageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = SizeConstants.MediumSize * 2),
                message = stringResource(id = R.string.summary_usage_and_diagnostics),
                learnMoreText = stringResource(id = R.string.learn_more),
                learnMoreUrl = AppLinks.PRIVACY_POLICY
            )
        }
    }
}