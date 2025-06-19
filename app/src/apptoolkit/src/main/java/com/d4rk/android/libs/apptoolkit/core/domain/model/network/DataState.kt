package com.d4rk.android.libs.apptoolkit.core.domain.model.network

typealias RootError = Error

sealed interface DataState<out D , out E : RootError> {
    data class Success<out D , out E : RootError>(val data : D) : DataState<D , E>
    data class Error<out D , out E : RootError>(val data : D? = null , val error : E) : DataState<D , E>
    data class Loading<out D , out E : RootError>(val data : D? = null) : DataState<D , E>
}

inline fun <D , E : RootError> DataState<D , E>.onSuccess(action : (D) -> Unit) : DataState<D , E> {
    return when (this) {
        is DataState.Success -> {
            action(data)
            this
        }

        else -> this
    }
}

inline fun <D , E : RootError> DataState<D , E>.onLoading(action : (D?) -> Unit) : DataState<D , E> {
    return when (this) {
        is DataState.Loading -> {
            action(data)
            this
        }

        else -> this
    }
}