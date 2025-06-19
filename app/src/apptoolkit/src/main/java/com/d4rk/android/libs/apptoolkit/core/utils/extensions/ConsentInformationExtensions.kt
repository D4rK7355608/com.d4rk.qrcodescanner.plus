package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import android.app.Activity
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ConsentFormHelper
import com.google.android.ump.ConsentInformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Launches the consent form if [ConsentInformation] is available.
 *
 * @param scope [CoroutineScope] used to launch the form loading on the provided dispatcher.
 * @param dispatcherProvider Provides the [kotlin.coroutines.CoroutineDispatcher] on which the form
 * will be loaded.
 * @param activity The activity used to show the consent form.
 * @param onFormShown Callback invoked after the form is shown or immediately if no form is
 * available.
 */
fun ConsentInformation?.loadAndShowIfNeeded(
    scope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    activity: Activity,
    onFormShown: () -> Unit = {},
) {
    this?.let { info ->
        scope.launch(context = dispatcherProvider.main) {
            ConsentFormHelper.showConsentFormIfRequired(activity = activity, consentInfo = info) {
                onFormShown()
            }
        }
    } ?: onFormShown()
}
