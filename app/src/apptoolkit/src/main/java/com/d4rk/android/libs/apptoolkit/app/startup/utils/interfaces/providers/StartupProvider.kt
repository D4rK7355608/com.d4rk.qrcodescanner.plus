package com.d4rk.android.libs.apptoolkit.app.startup.utils.interfaces.providers

import android.content.Context
import android.content.Intent
import com.google.android.ump.ConsentRequestParameters

interface StartupProvider {

    /** Which runtime permissions (if any) should we request? */
    val requiredPermissions : Array<String>

    /** Null ⇒ skip consent altogether. Otherwise the UMP builder parameters you want to use */
    val consentRequestParameters : ConsentRequestParameters?

    /** Once everything’s done, where do we go? */
    fun getNextIntent(context : Context) : Intent
}
