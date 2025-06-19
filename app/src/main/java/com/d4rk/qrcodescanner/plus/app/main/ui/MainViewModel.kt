package com.d4rk.qrcodescanner.plus.app.main.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import com.d4rk.qrcodescanner.plus.app.main.domain.action.MainAction
import com.d4rk.qrcodescanner.plus.app.main.domain.action.MainEvent
import com.d4rk.qrcodescanner.plus.app.main.domain.model.UiMainScreen
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.main.domain.usecases.PerformInAppUpdateUseCase
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.successData
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.coroutines.flow.flowOn

class MainViewModel(private val performInAppUpdateUseCase : PerformInAppUpdateUseCase , private val dispatcherProvider : DispatcherProvider) : ScreenViewModel<UiMainScreen , MainEvent , MainAction>(initialState = UiStateScreen(data = UiMainScreen())) {

    init {
        onEvent(event = MainEvent.LoadNavigation)
    }

    override fun onEvent(event : MainEvent) {
        when (event) {
            is MainEvent.LoadNavigation -> loadNavigationItems()
            is MainEvent.CheckForUpdates -> checkAppUpdate()
        }
    }

    private fun checkAppUpdate() {
        launch(context = dispatcherProvider.io) {
            performInAppUpdateUseCase(Unit).flowOn(dispatcherProvider.default).collect { result : DataState<Int , Errors> ->
                if (result is DataState.Error) {
                    screenState.showSnackbar<UiMainScreen>(snackbar = UiSnackbar(message = UiTextHelper.StringResource(R.string.snack_update_failed) , isError = true , timeStamp = System.currentTimeMillis() , type = ScreenMessageType.SNACKBAR))
                }
            }
        }
    }

    private fun loadNavigationItems() {
        launch(context = dispatcherProvider.default) {
            screenState.successData<UiMainScreen> {
                copy(
                    navigationDrawerItems = listOf(
                        NavigationDrawerItem(
                            title = R.string.settings , selectedIcon = Icons.Outlined.Settings
                        ) , NavigationDrawerItem(
                            title = R.string.help_and_feedback , selectedIcon = Icons.AutoMirrored.Outlined.HelpOutline
                        ) , NavigationDrawerItem(
                            title = R.string.updates , selectedIcon = Icons.AutoMirrored.Outlined.EventNote
                        ) , NavigationDrawerItem(
                            title = R.string.share , selectedIcon = Icons.Outlined.Share
                        )
                    )
                )
            }
        }
    }
}