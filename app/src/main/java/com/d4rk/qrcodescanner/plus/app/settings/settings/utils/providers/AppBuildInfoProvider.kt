package com.d4rk.qrcodescanner.plus.app.settings.settings.utils.providers

import android.content.Context
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.qrcodescanner.plus.BuildConfig

class AppBuildInfoProvider(val context : Context) : BuildInfoProvider {

    override val packageName : String get() = "AppToolkit"

    override val appVersion : String get() = BuildConfig.VERSION_NAME

    override val appVersionCode : Int
        get() {
            return BuildConfig.VERSION_CODE
        }

    override val isDebugBuild : Boolean
        get() {
            return BuildConfig.DEBUG
        }
}