@file:Suppress("DEPRECATION")

package com.d4rk.android.libs.apptoolkit.app.support.ui.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.d4rk.android.libs.apptoolkit.app.support.domain.actions.SupportEvent
import com.d4rk.android.libs.apptoolkit.app.support.ui.SupportViewModel

@Composable
fun rememberBillingClient(context : Context , viewModel : SupportViewModel) : BillingClient {
    val billingClient : BillingClient = remember {
        BillingClient.newBuilder(context).setListener { _ , _ -> }.enablePendingPurchases().build()
    }

    DisposableEffect(billingClient) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult : BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    viewModel.onEvent(event = SupportEvent.QueryProductDetails(billingClient = billingClient))
                }
            }

            override fun onBillingServiceDisconnected() {}
        })

        onDispose {
            billingClient.endConnection()
        }
    }
    return billingClient
}