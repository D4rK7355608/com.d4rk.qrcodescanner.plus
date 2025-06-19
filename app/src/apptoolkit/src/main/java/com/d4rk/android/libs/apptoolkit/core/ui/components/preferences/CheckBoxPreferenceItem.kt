package com.d4rk.android.libs.apptoolkit.core.ui.components.preferences

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
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
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * A composable function that creates a preference item with a checkbox.
 *
 * This item displays an optional icon, a title, an optional summary, and a checkbox.
 * Clicking the item toggles the checkbox state and triggers the provided [onCheckedChange] callback.
 *
 * @param icon The optional icon to display at the start of the item.
 * @param title The main title text for the preference item.
 * @param summary The optional summary text to display below the title.
 * @param checked The current checked state of the checkbox.
 * @param onCheckedChange A callback function that is invoked when the checkbox state changes.
 *                       It receives the new checked state as a boolean parameter.
 */
@Composable
fun CheckBoxPreferenceItem(icon : ImageVector? = null , title : String , summary : String? = null , checked : Boolean , onCheckedChange : (Boolean) -> Unit) {
    val view : View = LocalView.current
    Row(modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onCheckedChange(! checked)
            } , verticalAlignment = Alignment.CenterVertically) {
        icon?.let {
            LargeHorizontalSpacer()
            Icon(imageVector = it , contentDescription = null)
            LargeHorizontalSpacer()
        }
        Column(
            modifier = Modifier
                    .padding(all = SizeConstants.LargeSize)
                    .weight(weight = 1f)
        ) {
            Text(text = title , style = MaterialTheme.typography.titleMedium , maxLines = 1 , overflow = TextOverflow.Ellipsis , fontWeight = FontWeight.Bold)
            summary?.let {
                Text(text = it , style = MaterialTheme.typography.bodyMedium)
            }
        }
        Checkbox(checked = checked , onCheckedChange = { isChecked : Boolean ->
            onCheckedChange(isChecked)
        } , modifier = Modifier.padding(start = SizeConstants.LargeSize))
    }
}