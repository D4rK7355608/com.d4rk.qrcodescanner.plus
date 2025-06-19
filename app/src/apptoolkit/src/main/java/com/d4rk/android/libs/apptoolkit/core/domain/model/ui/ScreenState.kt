package com.d4rk.android.libs.apptoolkit.core.domain.model.ui

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.RootError
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenDataStatus
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class UiStateScreen<T>(
    val screenState : ScreenState = ScreenState.IsLoading() , var errors : List<UiSnackbar> = emptyList() , val snackbar : UiSnackbar? = null , val data : T? = null

) : UiState

data class UiSnackbar(
    var type : String = ScreenMessageType.NONE ,
    val message : UiTextHelper = UiTextHelper.DynamicString(content = "") ,
    val isError : Boolean = true ,
    val timeStamp : Long = 0 ,
)


inline fun <T> MutableStateFlow<UiStateScreen<T>>.updateData(
    newState : ScreenState , crossinline transform : (T) -> T
) {
    update { current ->
        val updatedData = current.data?.let { transform(it) }
        current.copy(screenState = newState , data = updatedData)
    }
}

inline fun <T> MutableStateFlow<UiStateScreen<T>>.copyData(crossinline transform : T.() -> T) {
    update { current ->
        val updatedData = current.data?.transform()
        current.copy(data = updatedData)
    }
}

inline fun <T> MutableStateFlow<UiStateScreen<T>>.successData(crossinline transform : T.() -> T) {
    update { current ->
        val updatedData = current.data?.transform()
        current.copy(screenState = ScreenState.Success() , data = updatedData)
    }
}

inline fun <D , T , E : RootError> MutableStateFlow<UiStateScreen<T>>.applyResult(
    result : DataState<D , E> , errorMessage : UiTextHelper = UiTextHelper.DynamicString("Something went wrong") , crossinline transform : (D , T) -> T
) {
    when (result) {
        is DataState.Success -> {
            successData {
                transform(result.data , this)
            }
        }

        is DataState.Error -> {
            setErrors(errors = listOf(element = UiSnackbar(message = errorMessage)))
            updateState(newValues = ScreenState.Error())
        }

        is DataState.Loading -> {
            setLoading()
        }
    }
}

fun <T> MutableStateFlow<UiStateScreen<T>>.updateState(newValues : ScreenState) {
    update { current : UiStateScreen<T> ->
        current.copy(screenState = newValues)
    }
}

fun <T> MutableStateFlow<UiStateScreen<T>>.setErrors(errors : List<UiSnackbar>) {
    update { current : UiStateScreen<T> ->
        current.copy(errors = errors)
    }
}

fun <T> MutableStateFlow<UiStateScreen<T>>.showSnackbar(snackbar : UiSnackbar) {
    update { current : UiStateScreen<T> ->
        current.copy(snackbar = snackbar)
    }
}

fun <T> MutableStateFlow<UiStateScreen<T>>.dismissSnackbar() {
    update { current : UiStateScreen<T> ->
        current.copy(snackbar = null)
    }
}

fun <T> MutableStateFlow<UiStateScreen<T>>.setLoading() {
    update { current ->
        current.copy(screenState = ScreenState.IsLoading())
    }
}

fun <T> MutableStateFlow<UiStateScreen<T>>.getData() : T {
    return value.data ?: throw IllegalStateException("Data is not available or null.")
}

fun <T> MutableStateFlow<UiStateScreen<T>>.getErrors() : List<UiSnackbar> {
    return value.errors
}

sealed class ScreenState {
    data class NoData(val data : String = ScreenDataStatus.NO_DATA) : ScreenState()
    data class IsLoading(val data : String = ScreenDataStatus.LOADING) : ScreenState()
    data class Success(val data : String = ScreenDataStatus.HAS_DATA) : ScreenState()
    data class Error(val data : String = ScreenDataStatus.ERROR) : ScreenState()
}