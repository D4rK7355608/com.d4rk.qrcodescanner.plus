package com.d4rk.android.libs.apptoolkit.app.settings.general.ui

import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.actions.GeneralSettingsAction
import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.actions.GeneralSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.model.ui.UiGeneralSettingsScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.setErrors
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.setLoading
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateData
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateState
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper

class GeneralSettingsViewModel : ScreenViewModel<UiGeneralSettingsScreen , GeneralSettingsEvent , GeneralSettingsAction>(initialState = UiStateScreen(data = UiGeneralSettingsScreen())) {

    override fun onEvent(event : GeneralSettingsEvent) {
        when (event) {
            is GeneralSettingsEvent.Load -> loadContent(contentKey = event.contentKey)
        }
    }

    private fun loadContent(contentKey : String?) {
        launch {
            screenState.setLoading()
            if (! contentKey.isNullOrBlank()) {
                screenState.updateData(newState = ScreenState.Success()) { current : UiGeneralSettingsScreen ->
                    current.copy(contentKey = contentKey)
                }
            }
            else {
                screenState.setErrors(errors = listOf(element = UiSnackbar(message = UiTextHelper.StringResource(resourceId = R.string.error_invalid_content_key))))
                screenState.updateState(newValues = ScreenState.NoData())
            }
        }
    }
}