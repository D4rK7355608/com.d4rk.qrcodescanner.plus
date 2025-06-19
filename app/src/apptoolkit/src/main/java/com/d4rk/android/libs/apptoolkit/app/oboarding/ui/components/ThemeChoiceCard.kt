package com.d4rk.android.libs.apptoolkit.app.oboarding.ui.components

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tonality
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.oboarding.domain.data.model.OnboardingThemeChoice
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun ThemeChoiceCard(
    choice : OnboardingThemeChoice , isSelected : Boolean , onClick : () -> Unit
) {
    val cardColors = if (isSelected) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer , contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
    else {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant , contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    val border = if (isSelected) {
        BorderStroke(SizeConstants.ExtraTinySize , MaterialTheme.colorScheme.primary)
    }
    else {
        BorderStroke(SizeConstants.ExtraTinySize / 2 , MaterialTheme.colorScheme.outlineVariant)
    }

    val view : View = LocalView.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick()
            .clip(RoundedCornerShape(SizeConstants.LargeSize))
            .clickable(onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onClick() }) , shape = RoundedCornerShape(SizeConstants.LargeSize) , colors = cardColors , elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) SizeConstants.ExtraSmallSize else SizeConstants.ExtraTinySize / 2) , border = border
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = SizeConstants.MediumSize * 2,
                    vertical = SizeConstants.LargeIncreasedSize
                )
                .fillMaxWidth() , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = choice.icon , contentDescription = choice.displayName , modifier = Modifier.size(SizeConstants.ExtraLargeIncreasedSize)
                )
                LargeHorizontalSpacer()
                Column {
                    Text(
                        text = choice.displayName ,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold) ,
                    )
                    Text(
                        text = choice.description , style = MaterialTheme.typography.bodyMedium , color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }
            AnimatedVisibility(
                visible = isSelected , enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn() , exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .size(SizeConstants.ExtraLargeSize)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary) , contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Tonality , contentDescription = stringResource(R.string.selected) , tint = MaterialTheme.colorScheme.onPrimary , modifier = Modifier.size(SizeConstants.SmallSize * 2 + SizeConstants.ExtraTinySize)
                    )
                }
            }
        }
    }
}