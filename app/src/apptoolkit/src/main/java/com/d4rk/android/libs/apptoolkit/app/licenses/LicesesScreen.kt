package com.d4rk.android.libs.apptoolkit.app.licenses

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.components.navigation.LargeTopAppBarWithScaffold
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.android.rememberLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicensesScreen(activity : Activity) {
    val scrollBehavior : TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())

    LargeTopAppBarWithScaffold(title = stringResource(id = R.string.oss_license_title) , onBackClicked = { activity.finish() } , scrollBehavior = scrollBehavior) { paddingValues ->
        val libraries: Libs? by rememberLibraries(resId = R.raw.aboutlibraries)

        LibrariesContainer(
            libraries = libraries,
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            padding = LibraryDefaults.libraryPadding(),
            dimensions = LibraryDefaults.libraryDimensions(),
            showDescription = true,
            showFundingBadges = true,
        )
    }
}