package com.d4rk.android.libs.apptoolkit.core.utils.interfaces

/**
 * Interface definition for a callback to be invoked when an ad display is completed.
 *
 * This interface provides a single method, [onShowAdComplete], which is called after an ad has finished displaying,
 * regardless of whether the user interacted with it or not. This can be used to perform actions after the ad has been displayed,
 * such as resuming gameplay, updating UI elements, or other necessary cleanup.
 */
interface OnShowAdCompleteListener {
    fun onShowAdComplete()
}