package com.d4rk.qrcodescanner.plus.ui.screens.settings.advanced

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.ui.components.preferences.PreferenceCategoryItem
import com.d4rk.android.libs.apptoolkit.ui.components.preferences.PreferenceItem
import com.d4rk.android.libs.apptoolkit.utils.helpers.IntentsHelper
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.ui.screens.settings.advanced.search.ChooseSearchEngineActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSettingsComposable(activity : AdvancedSettingsActivity) {
    val context : Context = LocalContext.current
    val view : View = LocalView.current
    val scrollBehavior : TopAppBarScrollBehavior =
            TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) , topBar = {
        LargeTopAppBar(title = { Text(stringResource(R.string.advanced)) } , navigationIcon = {
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
            item {
                PreferenceCategoryItem(title = stringResource(R.string.search_engines))
                PreferenceItem(title = stringResource(R.string.search_engines) ,
                               summary = stringResource(R.string.summary_preference_settings_search_engines) ,
                               onClick = {
                                   IntentsHelper.openActivity(
                                       context , ChooseSearchEngineActivity::class.java
                                   )
                               })
            }
            item {
                PreferenceCategoryItem(title = stringResource(R.string.error_reporting))
                PreferenceItem(title = stringResource(R.string.bug_report) ,
                               summary = stringResource(R.string.summary_preference_settings_bug_report) ,
                               onClick = {
                                   IntentsHelper.openUrl(
                                       context = context ,
                                       url = "https://github.com/D4rK7355608/${context.packageName}/issues/new"
                                   )
                               })
            }
        }
    }
}