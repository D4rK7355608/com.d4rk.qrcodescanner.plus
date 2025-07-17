package com.d4rk.qrcodescanner.plus.core.di.modules

import com.d4rk.android.libs.apptoolkit.data.client.KtorClient
import com.d4rk.android.libs.apptoolkit.data.core.ads.AdsCoreManager
import com.d4rk.qrcodescanner.plus.BuildConfig
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule : Module = module {
    single<AdsCoreManager> { AdsCoreManager(context = get() , get()) }
    single { KtorClient().createClient(enableLogging = BuildConfig.DEBUG) }
}