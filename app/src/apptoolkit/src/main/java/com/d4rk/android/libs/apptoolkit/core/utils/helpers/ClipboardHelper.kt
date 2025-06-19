package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build

/**
 *  A helper object for managing clipboard operations.
 */
object ClipboardHelper {

    /**
     * Copies the given [text] to the clipboard.
     *
     * @param context The context used to access the clipboard service.
     * @param label A user-visible label for the clip data.
     * @param text The text to be copied to the clipboard.
     * @param onShowSnackbar A callback function that will be executed after the text is copied to clipboard,
     *                     if the Android version is less than or equal to Android 12 (API Level 32).
     *                     This callback is typically used to display a visual confirmation (e.g., a Snackbar).
     */
    fun copyTextToClipboard(context : Context , label : String , text : String , onShowSnackbar : () -> Unit = {}) {
        val clipboard : ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip : ClipData = ClipData.newPlainText(label , text)
        clipboard.setPrimaryClip(clip)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            onShowSnackbar()
        }
    }
}
