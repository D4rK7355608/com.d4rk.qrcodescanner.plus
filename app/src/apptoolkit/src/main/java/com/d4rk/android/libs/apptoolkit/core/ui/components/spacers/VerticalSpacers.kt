package com.d4rk.android.libs.apptoolkit.core.ui.components.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun ExtraExtraLargeVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.ExtraExtraLargeSize))
}

@Composable
fun ExtraLargeIncreasedVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.ExtraLargeIncreasedSize))
}

/**
 * Creates a vertical spacer with extra-large height (28p).
 */
@Composable
fun ExtraLargeVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.ExtraLargeSize))
}

/**
 * Creates a vertical spacer with large increased height (20dp).
 */
@Composable
fun LargeIncreasedVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.LargeIncreasedSize))
}

/**
 * Creates a vertical spacer with large height (16dp).
 */
@Composable
fun LargeVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.LargeSize))
}

/**
 * Creates a vertical spacer with medium height (12dp).
 */
@Composable
fun MediumVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.MediumSize))
}

/**
 * Creates a vertical spacer with small height (8dp).
 */
@Composable
fun SmallVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.SmallSize))
}

/**
 * Creates a vertical spacer with extra small height (4dp).
 */
@Composable
fun ExtraSmallVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.ExtraSmallSize))
}

/**
 * Creates a vertical spacer with extra tiny height (2dp).
 */
@Composable
fun ExtraTinyVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.ExtraTinySize))
}

/**
 * Creates a vertical spacer with navigation bars padding.
 */
@Composable
fun NavigationBarsVerticalSpacer() {
    Spacer(modifier = Modifier.navigationBarsPadding())
}