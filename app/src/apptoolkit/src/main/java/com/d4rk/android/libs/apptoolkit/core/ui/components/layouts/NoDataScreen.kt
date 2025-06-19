package com.d4rk.android.libs.apptoolkit.core.ui.components.layouts

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.components.ads.AdBanner
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ButtonIconSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

/**
 * A composable function that displays a screen indicating that no data is available.
 * It can optionally display a retry button to allow the user to attempt to fetch data again.
 *
 * @param text The string resource ID for the text to be displayed. Defaults to R.string.try_again.
 * @param icon The [ImageVector] to be displayed as an icon. Defaults to Icons.Default.Info.
 * @param showRetry A boolean indicating whether to show the retry button. Defaults to false.
 * @param onRetry A lambda function to be executed when the retry button is clicked. Defaults to an empty lambda.
 *
 * Example Usage:
 * ```
 * NoDataScreen(text = R.string.no_items_found, icon = Icons.Default.Warning */
@Composable
fun NoDataScreen(
    text: Int = R.string.try_again,
    icon: ImageVector = Icons.Default.Info,
    showRetry: Boolean = false,
    onRetry: () -> Unit = {},
    showAd: Boolean = true,
    adsConfig: AdsConfig = koinInject(qualifier = named(name = "banner_medium_rectangle"))
) {
    val view: View = LocalView.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(size = SizeConstants.ExtraExtraLargeSize + SizeConstants.SmallSize + SizeConstants.ExtraTinySize)
                    .padding(bottom = SizeConstants.LargeSize),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(id = text),
                style = MaterialTheme.typography.displaySmall.copy(textAlign = TextAlign.Center),
                color = MaterialTheme.colorScheme.onBackground
            )
            if (showRetry) {
                LargeVerticalSpacer()
                Button(onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    onRetry()
                }, modifier = Modifier.bounceClick()) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(size = SizeConstants.ButtonIconSize)
                    )
                    ButtonIconSpacer()
                    Text(text = stringResource(id = text))
                }
            }

            LargeVerticalSpacer()

            if(showAd) {
                AdBanner(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = SizeConstants.MediumSize), adsConfig = adsConfig
                )
            }
        }
    }
}