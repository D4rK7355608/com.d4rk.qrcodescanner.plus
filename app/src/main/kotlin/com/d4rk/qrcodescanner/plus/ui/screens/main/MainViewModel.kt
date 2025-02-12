package com.d4rk.qrcodescanner.plus.ui.screens.main

import android.app.Activity
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.data.model.ui.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.utils.helpers.IntentsHelper
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.data.core.AppCoreManager
import com.d4rk.qrcodescanner.plus.data.model.ui.navigation.BottomNavigationScreen
import com.d4rk.qrcodescanner.plus.data.model.ui.screens.UiMainScreen
import com.d4rk.qrcodescanner.plus.notifications.managers.AppUpdateNotificationsManager
import com.d4rk.qrcodescanner.plus.ui.screens.main.repository.MainRepository
import com.d4rk.qrcodescanner.plus.ui.viewmodel.BaseViewModel
import com.google.android.play.core.appupdate.AppUpdateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application : Application) : BaseViewModel(application) {
    private val repository = MainRepository(
        dataStore = AppCoreManager.dataStore ,
        application = application
    )
    private val _uiState : MutableStateFlow<UiMainScreen> = MutableStateFlow(initializeUiState())
    val uiState : StateFlow<UiMainScreen> = _uiState

    fun checkForUpdates(activity : Activity , appUpdateManager : AppUpdateManager) {
        viewModelScope.launch(context = coroutineExceptionHandler) {
            repository.checkForUpdates(
                appUpdateManager = appUpdateManager , activity = activity
            )
        }
    }

    private fun initializeUiState() : UiMainScreen {
        return UiMainScreen(
            navigationDrawerItems = listOf(
                NavigationDrawerItem(
                    title = R.string.settings ,
                    selectedIcon = Icons.Outlined.Settings ,
                ) , NavigationDrawerItem(
                    title = R.string.help_and_feedback ,
                    selectedIcon = Icons.AutoMirrored.Outlined.HelpOutline ,
                ) , NavigationDrawerItem(
                    title = R.string.updates ,
                    selectedIcon = Icons.AutoMirrored.Outlined.EventNote ,
                ) , NavigationDrawerItem(
                    title = R.string.share ,
                    selectedIcon = Icons.Outlined.Share ,
                )
            ) , bottomNavigationItems = listOf(
                BottomNavigationScreen.Home ,
                BottomNavigationScreen.StudioBot ,
                BottomNavigationScreen.Favorites
            ) , currentBottomNavigationScreen = BottomNavigationScreen.Home
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkAndScheduleUpdateNotifications(appUpdateNotificationsManager : AppUpdateNotificationsManager) {
        viewModelScope.launch(context = coroutineExceptionHandler) {
            repository.checkAndScheduleUpdateNotificationsRepository(appUpdateNotificationsManager = appUpdateNotificationsManager)
        }
    }

    fun checkAppUsageNotifications() {
        viewModelScope.launch(context = coroutineExceptionHandler) {
            repository.checkAppUsageNotificationsRepository()
        }
    }

    fun checkAndHandleStartup() {
        viewModelScope.launch(context = coroutineExceptionHandler) {
            repository.checkAndHandleStartupRepository { isFirstTime ->
                if (isFirstTime) {
           /*         IntentsHelper.openActivity(
                        context = getApplication() , activityClass = StartupActivity::class.java
                    )*/
                }
            }
        }
    }

    fun configureSettings() {
        viewModelScope.launch(context = coroutineExceptionHandler) {
            repository.setupSettingsRepository()
        }
    }

    fun updateBottomNavigationScreen(newScreen : BottomNavigationScreen) {
        viewModelScope.launch(context = coroutineExceptionHandler) {
            _uiState.value = _uiState.value.copy(currentBottomNavigationScreen = newScreen)
        }
    }
}