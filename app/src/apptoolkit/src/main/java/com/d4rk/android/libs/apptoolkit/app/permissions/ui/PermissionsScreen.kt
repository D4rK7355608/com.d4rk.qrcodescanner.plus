package com.d4rk.android.libs.apptoolkit.app.permissions.ui

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.permissions.domain.actions.PermissionsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.components.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.PreferenceCategoryItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.SettingsPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraTinyVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen(viewModel : PermissionsViewModel) {
    val screenState : UiStateScreen<SettingsConfig> by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LargeTopAppBarWithScaffold(
        title = stringResource(id = R.string.permissions) , onBackClicked = { (context as Activity).finish() }) { paddingValues ->
        ScreenStateHandler(screenState = screenState , onLoading = { LoadingScreen() } , onEmpty = {
            NoDataScreen(
                icon = Icons.Outlined.Settings , showRetry = true , onRetry = {
                    viewModel.onEvent(PermissionsEvent.Load(context = context))
                })
        } , onSuccess = { settingsConfig ->
            PermissionsContent(paddingValues , settingsConfig)
        })
    }
}

@Composable
fun PermissionsContent(paddingValues : PaddingValues , settingsConfig : SettingsConfig) {
    LazyColumn(contentPadding = paddingValues , modifier = Modifier.fillMaxHeight()) {
        settingsConfig.categories.forEach { category ->
            item {
                category.title.let { title ->
                    PreferenceCategoryItem(title = title)
                    SmallVerticalSpacer()
                }
                Column(
                    modifier = Modifier
                            .padding(horizontal = SizeConstants.LargeSize)
                            .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
                ) {
                    category.preferences.forEach { preference ->
                        SettingsPreferenceItem(icon = preference.icon , title = preference.title , summary = preference.summary , onClick = { preference.action.invoke() })
                        ExtraTinyVerticalSpacer()
                    }
                }
            }
        }
    }
}