package com.d4rk.android.libs.apptoolkit.app.privacy.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.PrivacySettingsProvider
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.PreferenceCategoryItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.SettingsPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraTinyVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper

@Composable
fun PrivacySettingsList(paddingValues : PaddingValues = PaddingValues() , provider : PrivacySettingsProvider) {
    val context : Context = LocalContext.current

    LazyColumn(
        contentPadding = paddingValues , modifier = Modifier.fillMaxHeight()
    ) {
        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.privacy))
            SmallVerticalSpacer()
            Column(
                modifier = Modifier
                        .padding(horizontal = SizeConstants.LargeSize)
                        .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            ) {
                SettingsPreferenceItem(title = stringResource(id = R.string.privacy_policy) , summary = stringResource(id = R.string.summary_preference_settings_privacy_policy) , onClick = { IntentsHelper.openUrl(context , provider.privacyPolicyUrl) })
                ExtraTinyVerticalSpacer()
                SettingsPreferenceItem(title = stringResource(id = R.string.terms_of_service) , summary = stringResource(id = R.string.summary_preference_settings_terms_of_service) , onClick = { IntentsHelper.openUrl(context , provider.termsOfServiceUrl) })
                ExtraTinyVerticalSpacer()
                SettingsPreferenceItem(title = stringResource(id = R.string.code_of_conduct) , summary = stringResource(id = R.string.summary_preference_settings_code_of_conduct) , onClick = { IntentsHelper.openUrl(context , provider.codeOfConductUrl) })
                ExtraTinyVerticalSpacer()
                SettingsPreferenceItem(title = stringResource(id = R.string.permissions) , summary = stringResource(id = R.string.summary_preference_settings_permissions) , onClick = { provider.openPermissionsScreen() })
                ExtraTinyVerticalSpacer()
                SettingsPreferenceItem(title = stringResource(id = R.string.ads) , summary = stringResource(id = R.string.summary_preference_settings_ads) , onClick = { provider.openAdsScreen() })
                ExtraTinyVerticalSpacer()
                SettingsPreferenceItem(title = stringResource(id = R.string.usage_and_diagnostics) , summary = stringResource(id = R.string.summary_preference_settings_usage_and_diagnostics) , onClick = { provider.openUsageAndDiagnosticsScreen() })
            }
        }
        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.legal))
            SmallVerticalSpacer()
            Column(
                modifier = Modifier
                        .padding(horizontal = SizeConstants.LargeSize)
                        .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            ) {
                SettingsPreferenceItem(title = stringResource(id = R.string.legal_notices) , summary = stringResource(id = R.string.summary_preference_settings_legal_notices) , onClick = { IntentsHelper.openUrl(context , provider.legalNoticesUrl) })
                ExtraTinyVerticalSpacer()
                SettingsPreferenceItem(title = stringResource(id = R.string.license) , summary = stringResource(id = R.string.summary_preference_settings_license) , onClick = { IntentsHelper.openUrl(context , provider.licenseUrl) })
            }
        }
    }
}