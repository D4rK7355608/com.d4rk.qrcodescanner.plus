package com.d4rk.android.libs.apptoolkit.app.support.ui

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.support.domain.actions.SupportAction
import com.d4rk.android.libs.apptoolkit.app.support.domain.actions.SupportEvent
import com.d4rk.android.libs.apptoolkit.app.support.domain.model.UiSupportScreen
import com.d4rk.android.libs.apptoolkit.app.support.domain.usecases.QueryProductDetailsUseCase
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.applyResult
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.coroutines.flow.flowOn

class SupportViewModel(private val queryProductDetailsUseCase : QueryProductDetailsUseCase , private val dispatcherProvider : DispatcherProvider) : ScreenViewModel<UiSupportScreen , SupportEvent , SupportAction>(initialState = UiStateScreen(data = UiSupportScreen())) {

    override fun onEvent(event : SupportEvent) {
        when (event) {
            is SupportEvent.QueryProductDetails -> queryProductDetails(event.billingClient)
        }
    }

    private fun queryProductDetails(billingClient : BillingClient) {
        launch(context = dispatcherProvider.io) {
            queryProductDetailsUseCase(billingClient).flowOn(dispatcherProvider.default).collect { result : DataState<Map<String , ProductDetails> , Errors> ->
                screenState.applyResult(result = result , errorMessage = UiTextHelper.StringResource(R.string.error_failed_to_load_sku_details)) { productMap , current ->
                    current.copy(productDetails = productMap)
                }
            }
        }
    }
}