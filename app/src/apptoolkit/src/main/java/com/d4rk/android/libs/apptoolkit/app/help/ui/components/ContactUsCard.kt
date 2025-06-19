package com.d4rk.android.libs.apptoolkit.app.help.ui.components

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Support
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun ContactUsCard(onClick : () -> Unit) {
    val view : View = LocalView.current
    Card(modifier = Modifier
            .fillMaxWidth()
            .bounceClick()
            .clip(shape = RoundedCornerShape(size = SizeConstants.MediumSize))
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onClick()
            }) {
        Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = SizeConstants.LargeSize) , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center , modifier = Modifier.size(size = SizeConstants.ExtraExtraLargeSize)) {
                Icon(painter = painterResource(id = R.drawable.shape_scalloped) , contentDescription = null , modifier = Modifier.fillMaxSize() , tint = MaterialTheme.colorScheme.primaryContainer)
                Icon(imageVector = Icons.Outlined.Support , contentDescription = null , tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            LargeHorizontalSpacer()
            Column(
                modifier = Modifier
                        .weight(weight = 1f)
                        .fillMaxHeight()
            ) {
                Text(text = stringResource(id = R.string.contact_us) , fontWeight = FontWeight.Bold)
                Text(text = stringResource(id = R.string.contact_us_description))
            }
        }
    }
}