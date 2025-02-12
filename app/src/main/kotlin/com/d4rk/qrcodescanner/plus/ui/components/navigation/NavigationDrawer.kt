package com.d4rk.qrcodescanner.plus.ui.components.navigation

import android.content.Context
import android.view.View
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
import androidx.navigation.NavHostController
import com.d4rk.android.libs.apptoolkit.data.model.ui.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.ui.components.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.utils.helpers.IntentsHelper
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.data.datastore.DataStore
import com.d4rk.qrcodescanner.plus.data.model.ui.screens.UiMainScreen
import com.d4rk.qrcodescanner.plus.ui.components.modifiers.bounceClick
import com.d4rk.qrcodescanner.plus.ui.components.modifiers.hapticDrawerSwipe
import com.d4rk.qrcodescanner.plus.ui.screens.main.MainScreenContent
import com.d4rk.qrcodescanner.plus.ui.screens.main.MainViewModel
import com.d4rk.qrcodescanner.plus.ui.screens.settings.SettingsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    navHostController : NavHostController ,
    drawerState : DrawerState ,
    view : View ,
    dataStore : DataStore ,
    viewModel : MainViewModel ,
    context : Context
) {
    val uiState : UiMainScreen by viewModel.uiState.collectAsState()
    val drawerItems : List<NavigationDrawerItem> = uiState.navigationDrawerItems
    val scope : CoroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(modifier = Modifier.hapticDrawerSwipe(drawerState = drawerState) ,
                          drawerState = drawerState ,
                          drawerContent = {
                              ModalDrawerSheet {
                                  LargeVerticalSpacer()
                                  drawerItems.forEach { item ->
                                      val title = stringResource(id = item.title)
                                      NavigationDrawerItem(label = { Text(text = title) } ,
                                                           selected = false ,
                                                           onClick = {
                                                               when (item.title) {
                                                                   R.string.settings -> {
                                                                       IntentsHelper.openActivity(
                                                                           context = context ,
                                                                           activityClass = SettingsActivity::class.java
                                                                       )
                                                                   }

                                                                   R.string.help_and_feedback -> {
                                                                   /*    IntentsHelper.openActivity(
                                                                           context = context ,
                                                                           activityClass = HelpActivity::class.java
                                                                       )*/
                                                                   }

                                                                   R.string.updates -> {
                                                                       IntentsHelper.openUrl(
                                                                           context = context ,
                                                                           url = "https://github.com/D4rK7355608/${context.packageName}/blob/master/CHANGELOG.md"
                                                                       )
                                                                   }

                                                                   R.string.share -> {
                                                                       IntentsHelper.shareApp(
                                                                           context = context,
                                                                           shareMessageFormat = R.string.summary_share_message
                                                                       )
                                                                   }
                                                               }
                                                               scope.launch { drawerState.close() }
                                                           } ,
                                                           icon = {
                                                               Icon(
                                                                   item.selectedIcon ,
                                                                   contentDescription = title
                                                               )
                                                           } ,
                                                           badge = {
                                                               if (item.badgeText.isNotBlank()) {
                                                                   Text(text = item.badgeText)
                                                               }
                                                           } ,
                                                           modifier = Modifier
                                                                   .padding(
                                                                       paddingValues = NavigationDrawerItemDefaults.ItemPadding
                                                                   )
                                                                   .bounceClick())
                                  }
                              }
                          } ,
                          content = {
                              MainScreenContent(
                                  navHostController = navHostController ,
                                  dataStore = dataStore ,
                                  drawerState = drawerState ,
                                  context = context ,
                                  coroutineScope = scope ,
                                  viewModel = viewModel ,
                                  view = view
                              )
                          })
}