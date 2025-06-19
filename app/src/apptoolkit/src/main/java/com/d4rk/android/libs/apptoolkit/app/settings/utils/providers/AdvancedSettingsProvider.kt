package com.d4rk.android.libs.apptoolkit.app.settings.utils.providers

/**
 * Interface for providing advanced settings, typically used for
 * configuration or information that is not part of the core user experience
 * but may be useful for power users, developers, or for debugging.
 */
interface AdvancedSettingsProvider {
    val bugReportUrl : String
}