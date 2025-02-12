package com.d4rk.qrcodescanner.plus.ui.components.navigation

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.d4rk.android.libs.apptoolkit.data.model.ui.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.utils.helpers.IntentsHelper
import com.d4rk.qrcodescanner.plus.data.database.BarcodeDatabase
import com.d4rk.qrcodescanner.plus.data.datastore.DataStore
import com.d4rk.qrcodescanner.plus.data.model.ui.navigation.BottomNavigationScreen
import com.d4rk.qrcodescanner.plus.di.barcodeParser
import com.d4rk.qrcodescanner.plus.ui.screens.help.HelpActivity
import com.d4rk.qrcodescanner.plus.ui.screens.scan.ScanScreen
import com.d4rk.qrcodescanner.plus.ui.screens.settings.SettingsActivity
import com.d4rk.qrcodescanner.plus.utils.constants.ui.bottombar.BottomBarRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationHost(
    navHostController : NavHostController ,
    dataStore : DataStore ,
    paddingValues : PaddingValues ,
) {
    val context = LocalContext.current
    val startupPage : String =
            dataStore.getStartupPage().collectAsState(initial = BottomBarRoutes.SCAN).value
    val barcodeDatabase = remember { BarcodeDatabase.getInstance(context) }


    NavHost(navController = navHostController , startDestination = startupPage) {
        composable(route = BottomNavigationScreen.Home.route) {
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                ScanScreen(
                    barcodeParser = barcodeParser ,
                    barcodeDatabase = barcodeDatabase ,
                    navigateToBarcodeScreen = { /* TODO: */ } ,
                    handleScannedData = { _, _, _ -> /* TODO: */ } ,
                )
            }
        }
        composable(route = BottomNavigationScreen.StudioBot.route) {
            Box(modifier = Modifier.padding(paddingValues)) {
                // create screen
            }
        }
        composable(route = BottomNavigationScreen.Favorites.route) {
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                // history screen
            }
        }
    }
}

fun handleNavigationItemClick(
    context : Context , item : NavigationDrawerItem , drawerState : DrawerState , coroutineScope : CoroutineScope
) {
    when (item.title) {
        com.d4rk.android.libs.apptoolkit.R.string.settings -> IntentsHelper.openActivity(
            context = context , activityClass = SettingsActivity::class.java
        )

        com.d4rk.android.libs.apptoolkit.R.string.help_and_feedback -> IntentsHelper.openActivity(
            context = context , activityClass = HelpActivity::class.java
        )

        com.d4rk.android.libs.apptoolkit.R.string.updates -> IntentsHelper.openUrl(
            context = context , url = "https://github.com/D4rK7355608/${context.packageName}/blob/master/CHANGELOG.md"
        )

        com.d4rk.android.libs.apptoolkit.R.string.share -> IntentsHelper.shareApp(
            context = context , shareMessageFormat = com.d4rk.android.libs.apptoolkit.R.string.summary_share_message
        )
    }
    coroutineScope.launch { drawerState.close() }
}