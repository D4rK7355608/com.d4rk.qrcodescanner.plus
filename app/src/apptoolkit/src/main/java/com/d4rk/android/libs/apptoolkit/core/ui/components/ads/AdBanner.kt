package com.d4rk.android.libs.apptoolkit.core.ui.components.ads

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import org.koin.compose.koinInject

@Composable
fun AdBanner(modifier : Modifier = Modifier , adsConfig : AdsConfig , buildInfoProvider : BuildInfoProvider = koinInject()) {
    val context: Context = LocalContext.current
    val dataStore: CommonDataStore = CommonDataStore.getInstance(context = context)
    val showAds : Boolean by dataStore.ads(default = ! buildInfoProvider.isDebugBuild).collectAsState(initial = true)

    if (showAds) {
        AndroidView(
            modifier = modifier
                    .fillMaxWidth()
                    .height(height = adsConfig.adSize.height.dp) , factory = { adViewContext : Context ->
                AdView(adViewContext).apply {
                    setAdSize(adsConfig.adSize)
                    adUnitId = adsConfig.bannerAdUnitId
                    loadAd(AdRequest.Builder().build())
                }
            })
    }
}