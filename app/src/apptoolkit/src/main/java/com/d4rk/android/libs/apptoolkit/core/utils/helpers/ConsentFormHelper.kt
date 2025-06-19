package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.app.Activity
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

object ConsentFormHelper {

    fun showConsentFormIfRequired(
        activity : Activity ,
        consentInfo : ConsentInformation ,
        onFormShown : () -> Unit = {}
    ) {
        val params : ConsentRequestParameters =
            ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(false).build()

        consentInfo.requestConsentInfoUpdate(activity , params , {
            UserMessagingPlatform.loadConsentForm(activity , { consentForm : ConsentForm ->
                if (consentInfo.consentStatus == ConsentInformation.ConsentStatus.REQUIRED || consentInfo.consentStatus == ConsentInformation.ConsentStatus.UNKNOWN) {
                    consentForm.show(activity) { onFormShown() }
                }
            } , {})
        } , {})
    }

    fun showConsentForm(
        activity : Activity ,
        consentInfo : ConsentInformation ,
        onFormShown : () -> Unit = {}
    ) {
        val params : ConsentRequestParameters =
            ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(false).build()

        consentInfo.requestConsentInfoUpdate(activity , params , {
            UserMessagingPlatform.loadConsentForm(activity , { consentForm : ConsentForm ->
                consentForm.show(activity) { onFormShown() }
            } , {})
        } , {})
    }
}