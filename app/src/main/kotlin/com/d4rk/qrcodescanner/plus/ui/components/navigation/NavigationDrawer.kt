package com.d4rk.qrcodescanner.plus.ui.components.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.data.model.ui.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.ui.components.modifiers.hapticDrawerSwipe
import com.d4rk.android.libs.apptoolkit.ui.components.spacers.LargeVerticalSpacer
import com.d4rk.qrcodescanner.plus.data.model.ui.screens.MainScreenState
import com.d4rk.qrcodescanner.plus.ui.screens.main.MainScaffoldContent
import kotlinx.coroutines.CoroutineScope

@Composable
fun NavigationDrawer(
    mainScreenState : MainScreenState
) {
    val uiState by mainScreenState.viewModel.uiState.collectAsState()
    val drawerItems = uiState.navigationDrawerItems
    val coroutineScope : CoroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(modifier = Modifier.hapticDrawerSwipe(drawerState = mainScreenState.drawerState) , drawerState = mainScreenState.drawerState , drawerContent = {
        ModalDrawerSheet {
            LargeVerticalSpacer()
            drawerItems.forEach { item ->
                NavigationDrawerItemContent(
                    item = item , coroutineScope = coroutineScope , drawerState = mainScreenState.drawerState , context = mainScreenState.context
                )
            }
        }
    }) {
        MainScaffoldContent(
            mainScreenState = mainScreenState , coroutineScope = coroutineScope
        )
    }
}

@Composable
private fun NavigationDrawerItemContent(
    item : NavigationDrawerItem , coroutineScope : CoroutineScope , drawerState : DrawerState , context : Context
) {
    val title = stringResource(id = item.title)
    NavigationDrawerItem(label = { Text(text = title) } , selected = false , onClick = {
        handleNavigationItemClick(
            context = context , item = item , drawerState = drawerState , coroutineScope = coroutineScope
        )
    } , icon = {
        Icon(item.selectedIcon , contentDescription = title)
    } , badge = {
        if (item.badgeText.isNotBlank()) {
            Text(text = item.badgeText)
        }
    } , modifier = Modifier
            .padding(paddingValues = NavigationDrawerItemDefaults.ItemPadding)
            .bounceClick())
}