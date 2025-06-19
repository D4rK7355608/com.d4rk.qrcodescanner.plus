package com.d4rk.android.libs.apptoolkit.app.settings.utils.providers

import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks

interface PrivacySettingsProvider {

    /**
     * URL for the privacy policy.
     */
    val privacyPolicyUrl : String
        get() = AppLinks.PRIVACY_POLICY

    /**
     * URL for the terms of service.
     */
    val termsOfServiceUrl : String
        get() = AppLinks.TERMS_OF_SERVICE

    /**
     * URL for the code of conduct.
     */
    val codeOfConductUrl : String
        get() = AppLinks.CODE_OF_CONDUCT

    /**
     * URL for the legal notices.
     */
    val legalNoticesUrl : String
        get() = AppLinks.LEGAL_NOTICES

    /**
     * URL for the license (GPLv3).
     */
    val licenseUrl : String
        get() = AppLinks.GPL_V3

    fun openPermissionsScreen()
    fun openAdsScreen()
    fun openUsageAndDiagnosticsScreen()
}