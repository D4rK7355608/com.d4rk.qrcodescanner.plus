package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.components

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.switches.CustomSwitch
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun ConsentToggleCard(
    title: String,
    description: String,
    switchState: Boolean,
    icon: ImageVector,
    onCheckedChange: (Boolean) -> Unit
) {
    val view: View = LocalView.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick()
            .clip(shape = RoundedCornerShape(SizeConstants.LargeSize))
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onCheckedChange(!switchState)
            },
        shape = MaterialTheme.shapes.large, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = SizeConstants.LargeSize,
                        vertical = SizeConstants.MediumSize
                    ), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(R.string.icon_desc_consent_category),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(SizeConstants.LargeIncreasedSize + SizeConstants.ExtraSmallSize)
                )
                LargeHorizontalSpacer()
                Column(modifier = Modifier.weight(weight = 1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        modifier = Modifier.animateContentSize(),
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
                LargeHorizontalSpacer()
                CustomSwitch(
                    checked = switchState,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier,
                )
            }
        }
    }
}