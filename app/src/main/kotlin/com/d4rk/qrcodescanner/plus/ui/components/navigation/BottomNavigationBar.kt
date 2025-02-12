package com.d4rk.qrcodescanner.plus.ui.components.navigation

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.d4rk.android.libs.apptoolkit.ui.components.modifiers.bounceClick
import com.d4rk.qrcodescanner.plus.data.datastore.DataStore
import com.d4rk.qrcodescanner.plus.data.model.ui.navigation.BottomNavigationScreen
import com.d4rk.qrcodescanner.plus.data.model.ui.screens.UiMainScreen
import com.d4rk.qrcodescanner.plus.ui.components.ads.AdBannerFull
import com.d4rk.qrcodescanner.plus.ui.screens.main.MainViewModel

@Composable
fun BottomNavigationBar(
    navController : NavController ,
    viewModel : MainViewModel ,
    dataStore : DataStore ,
    view : View ,
) {
    val uiState : UiMainScreen by viewModel.uiState.collectAsState()
    val bottomBarItems : List<BottomNavigationScreen> = uiState.bottomNavigationItems
    val showLabels : Boolean = dataStore.getShowBottomBarLabels().collectAsState(initial = true).value

    Column {
        AdBannerFull(
            modifier = Modifier.fillMaxWidth()
        )
        NavigationBar {
            val navBackStackEntry : NavBackStackEntry? by navController.currentBackStackEntryAsState()
            val currentRoute : String? = navBackStackEntry?.destination?.route
            bottomBarItems.forEach { screen ->
                NavigationBarItem(icon = {
                    val iconResource : ImageVector =
                            if (currentRoute == screen.route) screen.selectedIcon else screen.icon
                    Icon(
                        imageVector = iconResource ,
                        modifier = Modifier.bounceClick() ,
                        contentDescription = null
                    )
                } , label = {
                    if (showLabels) Text(
                        text = stringResource(id = screen.title) ,
                        overflow = TextOverflow.Ellipsis ,
                        modifier = Modifier.basicMarquee()
                    )
                } , selected = currentRoute == screen.route , onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    if (currentRoute != screen.route) {
                        navController.navigate(route = screen.route) {
                            popUpTo(id = navController.graph.startDestinationId) {
                                saveState = false
                            }
                            launchSingleTop = true
                        }
                        viewModel.updateBottomNavigationScreen(newScreen = screen)
                    }
                })
            }
        }
    }
}