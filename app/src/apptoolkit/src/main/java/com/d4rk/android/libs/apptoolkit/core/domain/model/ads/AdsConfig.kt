package com.d4rk.android.libs.apptoolkit.core.domain.model.ads

import com.google.android.gms.ads.AdSize

data class AdsConfig(
    val bannerAdUnitId : String = "" ,
    val adSize : AdSize = AdSize.BANNER ,
)