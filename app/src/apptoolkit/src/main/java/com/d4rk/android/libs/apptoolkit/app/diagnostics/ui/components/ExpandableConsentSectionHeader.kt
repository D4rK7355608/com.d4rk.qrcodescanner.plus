package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.components

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun ExpandableConsentSectionHeader(
    title: String, expanded: Boolean, onToggle: () -> Unit
) {
    val view: View = LocalView.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick()
            .clickable(onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onToggle()
            })
            .padding(
                horizontal = SizeConstants.LargeSize,
                vertical = SizeConstants.MediumSize
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        IconButton(modifier = Modifier.bounceClick(), onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            onToggle()
        }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) stringResource(id = R.string.icon_desc_expand_less) else stringResource(
                    id = R.string.icon_desc_expand_more
                ),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}