package com.d4rk.android.libs.apptoolkit.app.help.ui.components

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun QuestionCard(title : String , summary : String , isExpanded : Boolean , onToggleExpand : () -> Unit , modifier : Modifier = Modifier) {
    val view: View = LocalView.current

    Card(modifier = modifier
        .bounceClick()
            .clip(shape = RoundedCornerShape(size = SizeConstants.MediumSize))
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onToggleExpand()
            }
            .padding(all = SizeConstants.LargeSize)
            .animateContentSize()
            .fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically , modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.QuestionAnswer , contentDescription = null , tint = MaterialTheme.colorScheme.primary , modifier = Modifier
                            .size(size = SizeConstants.ExtraExtraLargeSize)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer , shape = CircleShape
                            )
                            .padding(all = SizeConstants.SmallSize)
                )

                LargeHorizontalSpacer()

                Text(
                    text = title , style = MaterialTheme.typography.titleMedium , modifier = Modifier.weight(weight = 1f)
                )

                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore , contentDescription = null , tint = MaterialTheme.colorScheme.primary , modifier = Modifier.size(size = SizeConstants.LargeIncreasedSize + SizeConstants.ExtraSmallSize)
                )
            }
            if (isExpanded) {
                SmallVerticalSpacer()
                Text(
                    text = summary ,
                    style = MaterialTheme.typography.bodyMedium ,
                )
            }
        }
    }
}