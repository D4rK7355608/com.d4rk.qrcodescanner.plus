package com.d4rk.android.libs.apptoolkit.app.startup.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.fab.AnimatedExtendedFloatingActionButton
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.navigation.TopAppBarScaffold
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun StartupScreen(activity : StartupActivity) {

    TopAppBarScaffold(title = stringResource(R.string.welcome) , content = { paddingValues ->
        StartupScreenContent(paddingValues = paddingValues)
    } , floatingActionButton = {
        AnimatedExtendedFloatingActionButton(
            visible = activity.consentFormLoaded , modifier = Modifier.bounceClick() , containerColor = if (activity.consentFormLoaded) {
            FloatingActionButtonDefaults.containerColor
        }
        else {
            Gray
        } , text = { Text(text = stringResource(id = R.string.agree)) } , onClick = {
            activity.navigateToNext()
        } , icon = {
            Icon(
                imageVector = Icons.Outlined.CheckCircle , contentDescription = null
            )
        })
    })
}

@Composable
fun StartupScreenContent(paddingValues : PaddingValues) {
    Box(
        modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .padding(all = SizeConstants.MediumSize * 2)
                .safeDrawingPadding()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                AsyncImage(model = R.drawable.il_startup , contentDescription = null)
                InfoMessageSection(
                    message = stringResource(R.string.summary_browse_terms_of_service_and_privacy_policy),
                    learnMoreText = stringResource(R.string.learn_more),
                    learnMoreUrl = AppLinks.PRIVACY_POLICY
                )
            }
        }
    }
}