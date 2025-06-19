package com.d4rk.android.libs.apptoolkit.core.ui.base

import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow

abstract class ScreenViewModel<T , E : UiEvent , A : ActionEvent>(initialState : UiStateScreen<T>) : BaseViewModel<UiStateScreen<T> , E , A>(initialState) {
    open val screenState : MutableStateFlow<UiStateScreen<T>>
        get() = _uiState
    protected val screenData : T?
        get() = currentState.data
}