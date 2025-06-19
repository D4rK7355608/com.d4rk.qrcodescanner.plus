package com.d4rk.android.libs.apptoolkit.app.support.ui

import android.content.Context
import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Paid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.android.billingclient.api.BillingClient
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.support.domain.model.UiSupportScreen
import com.d4rk.android.libs.apptoolkit.app.support.ui.components.rememberBillingClient
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.ads.AdBanner
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ButtonIconSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportComposable(viewModel : SupportViewModel , activity : SupportActivity , adsConfig : AdsConfig = koinInject(qualifier = named(name = "banner_medium_rectangle"))) {
    val context : Context = LocalContext.current
    val billingClient : BillingClient = rememberBillingClient(context = context , viewModel = viewModel)
    val screenState : UiStateScreen<UiSupportScreen> by viewModel.uiState.collectAsState()

    LargeTopAppBarWithScaffold(
        title = stringResource(id = R.string.support_us) , onBackClicked = { activity.finish() }) { paddingValues ->
        ScreenStateHandler(screenState = screenState , onLoading = { LoadingScreen() } , onEmpty = { NoDataScreen() } , onSuccess = { supportData ->
            SupportScreenContent(paddingValues = paddingValues , activity = activity , supportData = supportData , adsConfig = adsConfig , context = context , billingClient = billingClient)
        })
    }
}

@Composable
fun SupportScreenContent(paddingValues : PaddingValues , activity : SupportActivity , supportData : UiSupportScreen , adsConfig : AdsConfig , context : Context , billingClient : BillingClient) {
    val view : View = LocalView.current

    Box(
        modifier = Modifier
                .padding(paddingValues)
                .fillMaxHeight()
    ) {
        LazyColumn {
            item {
                Text(
                    text = stringResource(id = R.string.paid_support) ,
                    modifier = Modifier.padding(start = SizeConstants.LargeSize , top = SizeConstants.LargeSize) ,
                    style = MaterialTheme.typography.titleLarge ,
                )
            }
            item {
                OutlinedCard(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = SizeConstants.LargeSize)
                ) {
                    Column {
                        Text(text = stringResource(id = R.string.summary_donations) , modifier = Modifier.padding(all = SizeConstants.LargeSize))
                        LazyRow(
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = SizeConstants.LargeSize) , horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            item {
                                FilledTonalButton(
                                    modifier = Modifier
                                            .fillMaxWidth()
                                            .bounceClick() , onClick = {
                                        view.playSoundEffect(SoundEffectConstants.CLICK)
                                        activity.initiatePurchase(productId = "low_donation" , productDetailsMap = supportData.productDetails , billingClient = billingClient)
                                    }) {
                                    Icon(imageVector = Icons.Outlined.Paid , contentDescription = null , modifier = Modifier.size(SizeConstants.ButtonIconSize))
                                    ButtonIconSpacer()
                                    Text(text = supportData.productDetails["low_donation"]?.oneTimePurchaseOfferDetails?.formattedPrice ?: "")
                                }
                            }
                            item {
                                FilledTonalButton(
                                    modifier = Modifier
                                            .fillMaxWidth()
                                            .bounceClick() , onClick = {
                                        view.playSoundEffect(SoundEffectConstants.CLICK)
                                        activity.initiatePurchase(productId = "normal_donation" , productDetailsMap = supportData.productDetails , billingClient = billingClient)
                                    }) {
                                    Icon(
                                        Icons.Outlined.Paid , contentDescription = null , modifier = Modifier.size(SizeConstants.ButtonIconSize)
                                    )
                                    ButtonIconSpacer()
                                    Text(text = supportData.productDetails["normal_donation"]?.oneTimePurchaseOfferDetails?.formattedPrice ?: "")
                                }
                            }
                        }
                        LazyRow(
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = SizeConstants.LargeSize) , horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            item {
                                FilledTonalButton(
                                    modifier = Modifier
                                            .fillMaxWidth()
                                            .bounceClick() , onClick = {
                                        view.playSoundEffect(SoundEffectConstants.CLICK)
                                        activity.initiatePurchase(productId = "high_donation" , productDetailsMap = supportData.productDetails , billingClient = billingClient)
                                    }) {
                                    Icon(
                                        Icons.Outlined.Paid , contentDescription = null , modifier = Modifier.size(SizeConstants.ButtonIconSize)
                                    )
                                    ButtonIconSpacer()
                                    Text(text = supportData.productDetails["high_donation"]?.oneTimePurchaseOfferDetails?.formattedPrice ?: "")
                                }
                            }
                            item {
                                FilledTonalButton(
                                    modifier = Modifier
                                            .fillMaxWidth()
                                            .bounceClick() , onClick = {
                                        view.playSoundEffect(SoundEffectConstants.CLICK)
                                        activity.initiatePurchase(productId = "extreme_donation" , productDetailsMap = supportData.productDetails , billingClient = billingClient)
                                    }) {
                                    Icon(
                                        Icons.Outlined.Paid , contentDescription = null , modifier = Modifier.size(SizeConstants.ButtonIconSize)
                                    )
                                    ButtonIconSpacer()
                                    Text(text = supportData.productDetails["extreme_donation"]?.oneTimePurchaseOfferDetails?.formattedPrice ?: "")
                                }
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    text = stringResource(id = R.string.non_paid_support) ,
                    modifier = Modifier.padding(start = SizeConstants.LargeSize) ,
                    style = MaterialTheme.typography.titleLarge ,
                )
            }
            item {
                FilledTonalButton(onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    IntentsHelper.openUrl(context = context , url = "https://direct-link.net/548212/agOqI7123501341")
                } , modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick()
                        .padding(all = SizeConstants.LargeSize)) {
                    Icon(imageVector = Icons.Outlined.Paid , contentDescription = null , modifier = Modifier.size(SizeConstants.ButtonIconSize))
                    ButtonIconSpacer()
                    Text(text = stringResource(id = R.string.web_ad))
                }
            }
            item {
                AdBanner(modifier = Modifier.padding(bottom = SizeConstants.MediumSize) , adsConfig = adsConfig)
            }
        }
    }
}