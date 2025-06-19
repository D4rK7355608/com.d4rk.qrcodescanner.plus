package com.d4rk.android.libs.apptoolkit.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel<S : UiState , E : UiEvent , A : ActionEvent>(initialState : S) : ViewModel() {

    protected val _uiState : MutableStateFlow<S> = MutableStateFlow(value = initialState)
    val uiState : StateFlow<S> = _uiState.asStateFlow()

    private val _actionEvent = Channel<A>()
    val actionEvent = _actionEvent.receiveAsFlow()

    protected val currentState : S
        get() = uiState.value

    protected fun launch(
        context : CoroutineContext = EmptyCoroutineContext , block : suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(context = context , block = block)

    abstract fun onEvent(event : E)

    protected fun sendAction(action : A) {
        launch {
            _actionEvent.send(action)
        }
    }
}