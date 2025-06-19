package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.content.Context
import android.widget.Toast
import com.d4rk.android.libs.apptoolkit.R

open class AppInfoHelper {

    /**
     * Checks if a specific app is installed on the device.
     * @return True if the app is installed, false otherwise.
     */
    fun isAppInstalled(context : Context , packageName : String) : Boolean = runCatching { context.packageManager.getApplicationInfo(packageName , 0) }.isSuccess

    /**
     * Opens a specific app if installed.
     * @return True if the app was opened, false otherwise.
     */
    fun openApp(context : Context , packageName : String) : Boolean = runCatching {
        context.packageManager.getLaunchIntentForPackage(packageName)?.let {
            context.startActivity(it)
            true
        } == true
    }.getOrElse {
        Toast.makeText(context , context.getString(R.string.app_not_installed) , Toast.LENGTH_SHORT).show()
        false
    }
}