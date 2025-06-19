package com.d4rk.android.libs.apptoolkit.app.support.domain.usecases

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.usecases.Repository
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.queryProductDetails
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.toError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QueryProductDetailsUseCase : Repository<BillingClient , Flow<DataState<Map<String , ProductDetails> , Errors>>> {
    override suspend fun invoke(param : BillingClient) : Flow<DataState<Map<String , ProductDetails> , Errors>> = flow {
        val productList = listOf(
            "low_donation" , "normal_donation" , "high_donation" , "extreme_donation"
        ).map {
            QueryProductDetailsParams.Product.newBuilder().setProductId(it).setProductType(BillingClient.ProductType.INAPP).build()
        }

        val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()

        param.queryProductDetails(params).fold(
            onSuccess = { details ->
                val resultMap = details.associateBy { pd -> pd.productId }
                emit(DataState.Success(data = resultMap))
            },
            onFailure = { error ->
                emit(DataState.Error(error = error.toError(default = Errors.UseCase.FAILED_TO_LOAD_SKU_DETAILS)))
            }
        )
    }
}