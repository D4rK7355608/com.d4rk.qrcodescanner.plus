package com.d4rk.android.libs.apptoolkit.core.ui.components.dialogs

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * Displays an AlertDialog containing application version information.
 *
 * This composable function presents a modal dialog displaying the application's name,
 * version, and copyright information. It leverages the [AlertDialog] composable
 * for the dialog UI and [VersionInfoAlertDialogContent] for the content.
 *
 * @param onDismiss Callback that is invoked when the dialog is dismissed (e.g., by clicking outside the dialog).
 * @param copyrightString The copyright string to display.
 * @param appName The string resource ID for the application's name.
 * @param versionName The application's version name (e.g., "1.0.0").
 * @param versionString The string resource ID for the version string to display before the versionName (e.g., "Version ").
 */
@Composable
fun VersionInfoAlertDialog(
    onDismiss : () -> Unit , copyrightString : Int , appName : Int , versionName : String , versionString : Int
) {
    AlertDialog(
        onDismissRequest = onDismiss ,
        text = {
            VersionInfoAlertDialogContent(copyrightString = copyrightString , appName = appName , versionName = versionName , versionString = versionString)
        } ,
        confirmButton = {} ,
    )
}

/**
 * Composable function that displays the version information in an alert dialog.
 *
 * This composable displays the application icon, name, version, and copyright information.
 *
 * @param copyrightString The copyright string to be displayed.
 * @param appName The resource ID of the application name string.
 * @param versionName The version name string.
 * @param versionString The resource ID of the version string format (e.g., "Version %s").
 */
@Composable
fun VersionInfoAlertDialogContent(copyrightString : Int , appName : Int , versionName : String , versionString : Int) {
    val context : Context = LocalContext.current
    val appIcon : Drawable = context.packageManager.getApplicationIcon(context.packageName)
    val imageLoader : ImageLoader = ImageLoader.Builder(context = context).build()

    Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(model = appIcon , contentDescription = null , modifier = Modifier.size(size = SizeConstants.ExtraExtraLargeSize) , imageLoader = imageLoader)
        LargeHorizontalSpacer()
        Column {
            Text(text = context.getString(appName) , style = MaterialTheme.typography.titleLarge)
            Text(text = stringResource(versionString , versionName) , style = MaterialTheme.typography.bodyMedium)
            LargeVerticalSpacer()
            Text(text = stringResource(id = copyrightString) , style = MaterialTheme.typography.bodyMedium)
        }
    }
}