package com.d4rk.qrcodescanner.plus.utils.constants.ads

import com.d4rk.android.libs.apptoolkit.utils.constants.ads.DebugAdsConstants
import com.d4rk.qrcodescanner.plus.BuildConfig


object AdsConstants {

    val BANNER_AD_UNIT_ID : String
        get() = if (BuildConfig.DEBUG) {
            DebugAdsConstants.BANNER_AD_UNIT_ID
        }
        else {
            "ca-app-pub-5294151573817700/8040893463"
        }

    val APP_OPEN_UNIT_ID : String
        get() = if (BuildConfig.DEBUG) {
            DebugAdsConstants.APP_OPEN_AD_UNIT_ID
        }
        else {
            "ca-app-pub-5294151573817700/9208287867"
        }
}