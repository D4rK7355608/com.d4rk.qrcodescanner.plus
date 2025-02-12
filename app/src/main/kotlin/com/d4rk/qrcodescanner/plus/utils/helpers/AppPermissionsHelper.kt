package com.d4rk.qrcodescanner.plus.utils.helpers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.d4rk.android.libs.apptoolkit.utils.helpers.PermissionsHelper
import com.d4rk.qrcodescanner.plus.utils.constants.permissions.AppPermissionsConstants

object AppPermissionsManager {
    private val permissionsHelper = PermissionsHelper

    val hasNotificationPermission: (Context) -> Boolean
        get() = permissionsHelper::hasNotificationPermission

    val requestNotificationPermission: (Activity) -> Unit
        get() = permissionsHelper::requestNotificationPermission

    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestCameraPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            AppPermissionsConstants.REQUEST_CODE_CAMERA_PERMISSION
        )
    }
}