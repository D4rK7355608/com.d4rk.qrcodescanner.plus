package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.content.Context
import android.content.res.Configuration

object ScreenHelper {

    /**
     * Checks if the device is in landscape orientation.
     *
     * @param context The context to access resources.
     * @return True if the device is in landscape, false otherwise.
     */
    fun isLandscape(context : Context) : Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * Checks if the device is a tablet.
     *
     * @param context The context to access resources.
     * @return True if the device is considered a tablet, false otherwise.
     */
    fun isTablet(context : Context) : Boolean {
        val screenWidthDp = context.resources.configuration.screenWidthDp
        return screenWidthDp >= 600 // Common threshold for tablets
    }

    /**
     * Combines checks for landscape orientation and tablet.
     *
     * @param context The context to access resources.
     * @return True if the device is either in landscape or is a tablet.
     */
    fun isLandscapeOrTablet(context : Context) : Boolean {
        return isLandscape(context) || isTablet(context)
    }
}