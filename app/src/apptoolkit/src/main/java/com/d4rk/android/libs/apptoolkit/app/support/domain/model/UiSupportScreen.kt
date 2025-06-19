package com.d4rk.android.libs.apptoolkit.app.support.domain.model

import com.android.billingclient.api.ProductDetails

data class UiSupportScreen(
    val productDetails : Map<String , ProductDetails> = emptyMap()
)