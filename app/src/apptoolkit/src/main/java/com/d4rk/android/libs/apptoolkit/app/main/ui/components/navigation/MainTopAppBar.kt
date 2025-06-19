package com.d4rk.android.libs.apptoolkit.app.main.ui.components.navigation

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.support.ui.SupportActivity
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.AnimatedButtonDirection
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(navigationIcon : ImageVector , onNavigationIconClick : () -> Unit , scrollBehavior : TopAppBarScrollBehavior) {
    val context : Context = LocalContext.current
    TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) } , navigationIcon = {
        AnimatedButtonDirection(
            icon = navigationIcon ,
            contentDescription = stringResource(id = R.string.go_back) ,
            onClick = {
                onNavigationIconClick()
            } ,
        )
    } , actions = {
        AnimatedButtonDirection(
            fromRight = true ,
            icon = Icons.Outlined.VolunteerActivism ,
            contentDescription = stringResource(id = R.string.go_back) ,
            onClick = {
                IntentsHelper.openActivity(context , SupportActivity::class.java)
            } ,
        )
    } , scrollBehavior = scrollBehavior)
}