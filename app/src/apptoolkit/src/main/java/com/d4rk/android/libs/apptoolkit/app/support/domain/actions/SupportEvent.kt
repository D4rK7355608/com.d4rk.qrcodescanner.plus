package com.d4rk.android.libs.apptoolkit.app.support.domain.actions

import com.android.billingclient.api.BillingClient
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface SupportEvent : UiEvent {
    data class QueryProductDetails(val billingClient : BillingClient) : SupportEvent
}