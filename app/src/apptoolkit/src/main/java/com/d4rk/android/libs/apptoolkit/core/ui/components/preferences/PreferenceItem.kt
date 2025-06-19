package com.d4rk.android.libs.apptoolkit.core.ui.components.preferences

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * Creates a clickable preference item for app preference screens.
 *
 * This composable function displays a preference item with an optional icon, title, and summary. The entire row is clickable and triggers the provided `onClick` callback function when clicked.
 *
 * @param icon An optional icon to be displayed at the start of the preference item. If provided, it should be an `ImageVector` object.
 * @param title An optional main title text displayed for the preference item.
 * @param summary An optional secondary text displayed below the title for additional information about the preference.
 * @param onClick A callback function that is called when the entire preference item is clicked. If no action is needed on click, this can be left empty.
 */
@Composable
fun PreferenceItem(icon : ImageVector? = null , title : String? = null , summary : String? = null , enabled : Boolean = true , rippleEffectDp : Dp = SizeConstants.LargeSize , onClick : () -> Unit = {}) {
    val view : View = LocalView.current
    Row(
        modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(size = rippleEffectDp))
                .clickable(enabled = enabled , onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    onClick()
                }) , verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            LargeHorizontalSpacer()
            Icon(imageVector = it , contentDescription = null)
        }
        Column(
            modifier = Modifier.padding(all = SizeConstants.LargeSize)
        ) {
            title?.let {
                Text(text = it , style = MaterialTheme.typography.titleMedium , fontWeight = FontWeight.Bold , color = if (! enabled) LocalContentColor.current.copy(alpha = 0.38f) else LocalContentColor.current , maxLines = 1 , overflow = TextOverflow.Ellipsis)
            }
            summary?.let {
                Text(text = it , style = MaterialTheme.typography.bodyMedium , color = if (! enabled) LocalContentColor.current.copy(alpha = 0.38f) else LocalContentColor.current)
            }
        }
    }
}

/**
 * A composable function that creates a settings preference item card.
 *
 * This function wraps a [PreferenceItem] composable inside a [Card] to provide a visually
 * distinct and interactive element for settings screens. It allows customization of the icon,
 * title, summary, ripple effect, and the action to perform when clicked.
 *
 * @param icon The optional [ImageVector] to display as an icon in the preference item.
 *             If null, no icon will be displayed.
 * @param title The optional [String] to display as the title of the preference item.
 *              If null, no title will be displayed.
 * @param summary The optional [String] to display as the summary of the preference item.
 *               If null, no summary will be displayed.
 * @param rippleEffectDp The [Dp] value to control the size of the ripple effect when the item is clicked.
 *                      Defaults to 2.dp.
 * @param onClick The lambda function to execute when the preference item is clicked.
 *                Defaults to an empty lambda, meaning no action will be performed by default.
 */
@Composable
fun SettingsPreferenceItem(icon : ImageVector? = null , title : String? = null , summary : String? = null , rippleEffectDp : Dp = SizeConstants.ExtraTinySize , onClick : () -> Unit = {}) {
    Card(
        modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(size = SizeConstants.ExtraTinySize)) ,
        shape = RoundedCornerShape(size = SizeConstants.ExtraTinySize) ,
    ) {
        PreferenceItem(rippleEffectDp = rippleEffectDp , icon = icon , title = title , summary = summary , onClick = {
            onClick()
        })
    }
}