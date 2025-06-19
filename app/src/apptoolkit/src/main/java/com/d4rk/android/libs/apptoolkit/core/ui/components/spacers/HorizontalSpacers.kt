package com.d4rk.android.libs.apptoolkit.core.ui.components.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun ExtraExtraLargeHorizontalSpacer() {
    Spacer(modifier = Modifier.width(width = SizeConstants.ExtraExtraLargeSize))
}

@Composable
fun ExtraLargeIncreasedHorizontalSpacer() {
    Spacer(modifier = Modifier.width(width = SizeConstants.ExtraLargeIncreasedSize))
}

/**
 * Creates a horizontal spacer with extra-large width (28dp).
 */
@Composable
fun ExtraLargeHorizontalSpacer() {
    Spacer(modifier = Modifier.width(width = SizeConstants.ExtraLargeSize))
}

@Composable
fun LargeIncreasedHorizontalSpacer() {
    Spacer(modifier = Modifier.width(width = SizeConstants.LargeIncreasedSize))
}

/**
 * Creates a horizontal spacer with large width (16dp).
 */
@Composable
fun LargeHorizontalSpacer() {
    Spacer(modifier = Modifier.width(width = SizeConstants.LargeSize))
}

/**
 * Creates a horizontal spacer with medium width (12dp).
 */
@Composable
fun MediumHorizontalSpacer() {
    Spacer(modifier = Modifier.width(width = SizeConstants.MediumSize))
}

/**
 * Creates a horizontal spacer with small width (8dp).
 */
@Composable
fun SmallHorizontalSpacer() {
    Spacer(modifier = Modifier.width(width = SizeConstants.SmallSize))
}

/**
 * Creates a horizontal spacer with extra small width (4dp).
 */
@Composable
fun ExtraSmallHorizontalSpacer() {
    Spacer(modifier = Modifier.width(width = SizeConstants.ExtraSmallSize))
}

/**
 * Creates a horizontal spacer with extra tiny height (2dp).
 */
@Composable
fun ExtraTinyHorizontalSpacer() {
    Spacer(modifier = Modifier.width(width = SizeConstants.ExtraTinySize))
}