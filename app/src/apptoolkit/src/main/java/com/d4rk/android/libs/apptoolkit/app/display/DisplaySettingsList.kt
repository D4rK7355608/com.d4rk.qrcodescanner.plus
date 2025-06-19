package com.d4rk.android.libs.apptoolkit.app.display

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.display.components.dialogs.SelectLanguageAlertDialog
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.DisplaySettingsProvider
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.PreferenceCategoryItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.PreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.SettingsPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.SwitchPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.SwitchPreferenceItemWithDivider
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraTinyVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DisplaySettingsList(paddingValues : PaddingValues = PaddingValues() , provider : DisplaySettingsProvider) {
    val coroutineScope : CoroutineScope = rememberCoroutineScope()
    val context : Context = LocalContext.current
    val dataStore: CommonDataStore = CommonDataStore.getInstance(context = context)
    var showLanguageDialog : Boolean by remember { mutableStateOf(value = false) }
    var showStartupDialog : Boolean by remember { mutableStateOf(value = false) }

    val currentThemeModeKey : String by dataStore.themeMode.collectAsState(initial = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM)
    val isSystemDarkTheme : Boolean = isSystemInDarkTheme()

    val isDarkThemeActive : Boolean = when (currentThemeModeKey) {
        DataStoreNamesConstants.THEME_MODE_DARK -> true
        DataStoreNamesConstants.THEME_MODE_LIGHT -> false

        else -> isSystemDarkTheme
    }

    val themeSummary : String = when (currentThemeModeKey) {
        DataStoreNamesConstants.THEME_MODE_DARK , DataStoreNamesConstants.THEME_MODE_LIGHT ->
            stringResource(id = R.string.will_never_turn_on_automatically)

        else ->
            stringResource(id = R.string.will_turn_on_automatically_by_system)
    }

    val isDynamicColors : State<Boolean> = dataStore.dynamicColors.collectAsState(initial = true)
    val bouncyButtons : Boolean by dataStore.bouncyButtons.collectAsState(initial = true)

    LazyColumn(contentPadding = paddingValues , modifier = Modifier.fillMaxHeight()) {
        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.appearance))
            SmallVerticalSpacer()

            Column(
                modifier = Modifier
                        .padding(horizontal = SizeConstants.LargeSize)
                        .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            ) {
                SwitchPreferenceItemWithDivider(title = stringResource(id = R.string.dark_theme) , summary = themeSummary , checked = isDarkThemeActive , onCheckedChange = { isChecked ->
                    coroutineScope.launch {
                        if (isChecked) {
                            dataStore.saveThemeMode(mode = DataStoreNamesConstants.THEME_MODE_DARK)
                            dataStore.themeModeState.value = DataStoreNamesConstants.THEME_MODE_DARK

                        }
                        else {
                            dataStore.saveThemeMode(mode = DataStoreNamesConstants.THEME_MODE_LIGHT)
                            dataStore.themeModeState.value = DataStoreNamesConstants.THEME_MODE_LIGHT

                        }
                    }
                } , onSwitchClick = { isChecked : Boolean ->
                    coroutineScope.launch {
                        if (isChecked) {
                            dataStore.saveThemeMode(mode = DataStoreNamesConstants.THEME_MODE_DARK)
                            dataStore.themeModeState.value = DataStoreNamesConstants.THEME_MODE_DARK

                        }
                        else {
                            dataStore.saveThemeMode(mode = DataStoreNamesConstants.THEME_MODE_LIGHT)
                            dataStore.themeModeState.value = DataStoreNamesConstants.THEME_MODE_LIGHT

                        }
                    }
                } , onClick = {
                    provider.openThemeSettings()
                })

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ExtraTinyVerticalSpacer()

                    SwitchPreferenceItem(
                        title = stringResource(id = R.string.dynamic_colors) ,
                        summary = stringResource(id = R.string.summary_preference_settings_dynamic_colors) ,
                        checked = isDynamicColors.value ,
                    ) { isChecked ->
                        coroutineScope.launch {
                            dataStore.saveDynamicColors(isChecked = isChecked)
                        }
                    }
                }
            }
        }
        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.app_behavior))
            SmallVerticalSpacer()

            Column(
                modifier = Modifier
                        .padding(horizontal = SizeConstants.LargeSize)
                        .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            ) {
                SwitchPreferenceItem(
                    title = stringResource(id = R.string.bounce_buttons) ,
                    summary = stringResource(id = R.string.summary_preference_settings_bounce_buttons) ,
                    checked = bouncyButtons ,
                ) { isChecked ->
                    coroutineScope.launch {
                        dataStore.saveBouncyButtons(isChecked = isChecked)
                    }
                }
            }
        }

        if (provider.supportsStartupPage) {
            item {
                PreferenceCategoryItem(title = stringResource(id = R.string.navigation))
                SmallVerticalSpacer()

                Column(
                    modifier = Modifier
                            .padding(horizontal = SizeConstants.LargeSize)
                            .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
                ) {
                    PreferenceItem(title = stringResource(id = R.string.startup_page) , summary = stringResource(id = R.string.summary_preference_settings_startup_page) , onClick = { showStartupDialog = true })
                    if (showStartupDialog) {
                        provider.StartupPageDialog(onDismiss = {
                            showStartupDialog = false
                        }) { }
                    }
                }
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.language))
            SmallVerticalSpacer()

            Column(
                modifier = Modifier
                        .padding(horizontal = SizeConstants.LargeSize)
                        .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            ) {
                SettingsPreferenceItem(title = stringResource(id = R.string.language) , summary = stringResource(id = R.string.summary_preference_settings_language) , onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val localeIntent : Intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS).setData(
                            Uri.fromParts(
                                "package" , context.packageName , null
                            )
                        )
                        val detailsIntent : Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                            Uri.fromParts(
                                "package" , context.packageName , null
                            )
                        )
                        when {
                            context.packageManager.resolveActivity(
                                localeIntent , 0
                            ) != null -> context.startActivity(localeIntent)

                            context.packageManager.resolveActivity(
                                detailsIntent , 0
                            ) != null -> context.startActivity(detailsIntent)

                            else -> {
                                showLanguageDialog = true
                            }
                        }
                    }
                    else {
                        showLanguageDialog = true
                    }
                })
            }

            if (showLanguageDialog) {
                SelectLanguageAlertDialog(onDismiss = { showLanguageDialog = false } , onLanguageSelected = { newLanguageCode : String ->
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(newLanguageCode))
                })
            }
        }
    }
}