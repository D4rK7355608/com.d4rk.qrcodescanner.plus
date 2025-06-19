package com.d4rk.qrcodescanner.plus.app.settings.settings.utils.providers

import android.content.Context
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.permissions.utils.interfaces.PermissionsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsCategory
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsPreference

class PermissionsSettingsProvider : PermissionsProvider {
    override fun providePermissionsConfig(context : Context) : SettingsConfig {
        return SettingsConfig(
            title = context.getString(R.string.permissions) , categories = listOf(
                SettingsCategory(
                    title = context.getString(R.string.normal) ,
                    preferences = listOf(
                        SettingsPreference(
                            title = context.getString(R.string.access_network_state) ,
                            summary = context.getString(R.string.summary_preference_permissions_access_network_state) ,
                        ) ,
                        SettingsPreference(
                            title = context.getString(R.string.ad_id) ,
                            summary = context.getString(R.string.summary_preference_permissions_ad_id) ,
                        ) ,
                        SettingsPreference(
                            title = context.getString(R.string.billing) ,
                            summary = context.getString(R.string.summary_preference_permissions_billing) ,
                        ) ,
                        SettingsPreference(
                            title = context.getString(R.string.check_license) ,
                            summary = context.getString(R.string.summary_preference_permissions_check_license) ,
                        ) ,
                        SettingsPreference(
                            title = context.getString(R.string.foreground_service) ,
                            summary = context.getString(R.string.summary_preference_permissions_foreground_service) ,
                        ) ,
                        SettingsPreference(
                            title = context.getString(R.string.internet) ,
                            summary = context.getString(R.string.summary_preference_permissions_internet) ,
                        ) ,
                        SettingsPreference(
                            title = context.getString(R.string.wake_lock) ,
                            summary = context.getString(R.string.summary_preference_permissions_wake_lock) ,
                        ) ,
                    ) ,
                ) , SettingsCategory(
                    title = context.getString(R.string.runtime) ,
                    preferences = listOf(
                        SettingsPreference(
                            title = context.getString(R.string.post_notifications) ,
                            summary = context.getString(R.string.summary_preference_permissions_post_notifications) ,
                        ) ,
                    ) ,
                ) , SettingsCategory(
                    title = context.getString(R.string.special) ,
                    preferences = listOf(
                        SettingsPreference(
                            title = context.getString(R.string.access_notification_policy) ,
                            summary = context.getString(R.string.summary_preference_permissions_access_notification_policy) ,
                        ) ,
                    ) ,
                )
            )
        )
    }
}