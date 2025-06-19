package com.d4rk.android.libs.apptoolkit.core.domain.model.error

/**
 * Data class representing the UI error state.
 *
 * This class is used to manage and communicate error states within the user interface.
 * It includes flags to indicate whether an error dialog should be shown and the associated error message.
 *
 * @property showErrorDialog Boolean flag indicating whether an error dialog should be displayed. Defaults to `false`.
 * @property errorMessage The error message to be displayed. Defaults to an empty string.
 */
data class UiErrorModel(
    val showErrorDialog : Boolean = false , val errorMessage : String = ""
)