package com.d4rk.android.libs.apptoolkit.app.settings.settings.ui

import android.app.Activity
import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ContactSupport
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.help.ui.HelpActivity
import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.actions.GeneralSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.general.ui.GeneralSettingsContent
import com.d4rk.android.libs.apptoolkit.app.settings.general.ui.GeneralSettingsViewModel
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.actions.SettingsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsCategory
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsPreference
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.GeneralSettingsContentProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.SettingsPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraTinyVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ScreenHelper
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel : SettingsViewModel , contentProvider : GeneralSettingsContentProvider) {
    val screenState : UiStateScreen<SettingsConfig> by viewModel.uiState.collectAsState()
    val context : Context = LocalContext.current

    LargeTopAppBarWithScaffold(title = stringResource(id = R.string.settings) , onBackClicked = { (context as Activity).finish() }) { paddingValues ->
        ScreenStateHandler(screenState = screenState , onLoading = { LoadingScreen() } , onEmpty = {
            NoDataScreen(icon = Icons.Outlined.Settings , showRetry = true , onRetry = {
                viewModel.onEvent(event = SettingsEvent.Load(context = context))
            })
        } , onSuccess = { config : SettingsConfig ->
            SettingsScreenContent(paddingValues = paddingValues , settingsConfig = config , contentProvider = contentProvider)
        })
    }
}

@Composable
fun SettingsScreenContent(paddingValues : PaddingValues , settingsConfig : SettingsConfig , contentProvider : GeneralSettingsContentProvider) {
    if (ScreenHelper.isLandscapeOrTablet(context = LocalContext.current)) {
        TabletSettingsScreen(paddingValues = paddingValues , settingsConfig = settingsConfig , contentProvider = contentProvider)
    }
    else {
        PhoneSettingsScreen(paddingValues = paddingValues , settingsConfig = settingsConfig)
    }
}

@Composable
fun PhoneSettingsScreen(paddingValues : PaddingValues , settingsConfig : SettingsConfig) {
    SettingsList(paddingValues = paddingValues , settingsConfig = settingsConfig) { pref : SettingsPreference ->
        pref.action()
    }
}

@Composable
fun TabletSettingsScreen(paddingValues : PaddingValues , settingsConfig : SettingsConfig , contentProvider : GeneralSettingsContentProvider) {
    var selected : SettingsPreference? by remember { mutableStateOf(value = null) }

    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(weight = 1f)) {
            SettingsList(paddingValues = paddingValues , settingsConfig = settingsConfig) { selected = it }
        }
        Box(modifier = Modifier.weight(weight = 2f)) {
            AnimatedContent(targetState = selected) { pref : SettingsPreference? ->
                pref?.let { SettingsDetail(preference = it , contentProvider = contentProvider , paddingValues = paddingValues) } ?: SettingsDetailPlaceholder(paddingValues = paddingValues)
            }
        }
    }
}

@Composable
fun SettingsDetailPlaceholder(paddingValues : PaddingValues) {
    val context : Context = LocalContext.current
    LazyColumn(contentPadding = paddingValues , modifier = Modifier.fillMaxHeight()) {
        item {
            Card(
                modifier = Modifier
                        .padding(top = SizeConstants.LargeSize , end = SizeConstants.LargeSize)
                        .fillMaxSize()
                        .wrapContentHeight() , shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize)
            ) {
                Column(modifier = Modifier.padding(all = SizeConstants.MediumSize * 2) , horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center) {
                    AsyncImage(
                        model = R.drawable.il_settings , contentDescription = null , modifier = Modifier
                                .size(size = 258.dp)
                                .fillMaxWidth()
                    )
                    LargeVerticalSpacer()
                    Text(text = stringResource(id = R.string.app_name) , style = MaterialTheme.typography.titleMedium , textAlign = TextAlign.Center)
                    SmallVerticalSpacer()
                    Text(text = stringResource(id = R.string.settings_placeholder_description) , style = MaterialTheme.typography.bodyMedium , textAlign = TextAlign.Center)
                }
                OutlinedButton(modifier = Modifier
                        .padding(all = SizeConstants.MediumSize * 2)
                        .align(alignment = Alignment.Start)
                        .bounceClick() , onClick = { IntentsHelper.openActivity(context = context , activityClass = HelpActivity::class.java) }) {
                    Icon(imageVector = Icons.AutoMirrored.Outlined.ContactSupport , contentDescription = null)
                    ExtraTinyVerticalSpacer()
                    Text(text = stringResource(id = R.string.get_help))
                }
            }
        }
    }
}

@Composable
fun SettingsDetail(preference : SettingsPreference , paddingValues : PaddingValues , contentProvider : GeneralSettingsContentProvider) {
    val viewModel : GeneralSettingsViewModel = koinViewModel()
    val snackbarHostState : SnackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = preference.key) {
        viewModel.onEvent(event = GeneralSettingsEvent.Load(contentKey = preference.key))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GeneralSettingsContent(viewModel = viewModel , contentProvider = contentProvider , paddingValues = paddingValues , snackbarHostState = snackbarHostState)
    }
}

@Composable
fun SettingsList(paddingValues : PaddingValues , settingsConfig : SettingsConfig , onPreferenceClick : (SettingsPreference) -> Unit = {}) {
    LazyColumn(contentPadding = paddingValues , modifier = Modifier.fillMaxHeight()) {
        settingsConfig.categories.forEach { category : SettingsCategory ->
            item {
                LargeVerticalSpacer()
                Column(
                    Modifier
                            .padding(horizontal = SizeConstants.LargeSize)
                            .clip(shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize))
                ) {
                    category.preferences.forEach { pref : SettingsPreference ->
                        SettingsPreferenceItem(icon = pref.icon , title = pref.title , summary = pref.summary , onClick = { onPreferenceClick(pref) })
                        ExtraTinyVerticalSpacer()
                    }
                }
            }
        }
    }
}