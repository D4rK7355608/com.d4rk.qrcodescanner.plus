package com.d4rk.qrcodescanner.plus.core.di.modules

import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.qrcodescanner.plus.app.settings.settings.utils.providers.AppBuildInfoProvider
import org.koin.dsl.module

val settingsModule = module {
    single<BuildInfoProvider> { AppBuildInfoProvider(context = get()) }
}