package com.d4rk.android.libs.apptoolkit.app.settings.utils.providers

interface BuildInfoProvider {
    val appVersion : String
    val appVersionCode : Int
    val packageName : String
    val isDebugBuild : Boolean
}