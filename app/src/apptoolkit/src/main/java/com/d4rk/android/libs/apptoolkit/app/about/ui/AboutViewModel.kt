package com.d4rk.android.libs.apptoolkit.app.about.ui

import android.content.Context
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.actions.AboutEvents
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.events.AboutActions
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.ui.UiAboutScreen
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateData
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.AboutLibrariesHelper
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper

open class AboutViewModel(private val dispatcherProvider : DispatcherProvider) : ScreenViewModel<UiAboutScreen , AboutEvents , AboutActions>(initialState = UiStateScreen(data = UiAboutScreen())) {

    override fun onEvent(event : AboutEvents) {
        when (event) {
            is AboutEvents.CopyDeviceInfo -> copyDeviceInfo()
            is AboutEvents.DismissSnackbar -> screenState.dismissSnackbar()
            is AboutEvents.LoadHtml -> loadHtmlData(
                context = event.context , packageName = event.packageName , versionName = event.versionName
            )
        }
    }

    protected open fun loadHtmlData(context : Context , packageName : String , versionName : String) {
        launch(dispatcherProvider.default) {
            val (changelog: String?, eula: String?) = AboutLibrariesHelper.loadHtmlData(
                packageName = packageName , currentVersionName = versionName , context = context
            )
            updateUi {
                copy(changelogHtml = changelog , eulaHtml = eula)
            }
        }
    }

    private fun copyDeviceInfo() {
        updateUi {
            copy(showDeviceInfoCopiedSnackbar = true)
        }
        screenState.showSnackbar<UiAboutScreen>(snackbar = UiSnackbar(message = UiTextHelper.StringResource(
            resourceId = R.string.snack_device_info_copied
        ) , isError = false , timeStamp = System.currentTimeMillis() , type = ScreenMessageType.SNACKBAR))
    }

    private inline fun updateUi(crossinline transform : UiAboutScreen.() -> UiAboutScreen) {
        launch {
            screenState.updateData<UiAboutScreen>(newState = screenState.value.screenState) { current: UiAboutScreen -> transform(current) }
        }
    }
}