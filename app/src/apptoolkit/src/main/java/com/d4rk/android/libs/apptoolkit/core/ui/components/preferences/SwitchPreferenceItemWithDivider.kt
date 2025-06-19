package com.d4rk.android.libs.apptoolkit.core.ui.components.preferences

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraSmallHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.switches.CustomSwitch
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * Creates a clickable preference item with a switch and a divider for app preference screens.
 *
 * This composable function combines an optional icon, title, summary, switch, and a divider into a single row.
 * The entire row is clickable and triggers the provided `onClick` callback function when clicked.
 * The switch is toggled on or off based on the `checked` parameter, and any change in its state calls
 * the `onCheckedChange` callback with the new state.
 *
 * @param icon An optional icon to be displayed at the start of the preference item. If provided, it should be an `ImageVector` object.
 * @param title The main title text displayed for the preference item.
 * @param summary A secondary text displayed below the title for additional information about the preference.
 * @param checked The initial state of the switch. Set to true for on and false for off.
 * @param onCheckedChange A callback function that is called whenever the switch is toggled. This function receives the new state of the switch (boolean) as a parameter.
 * @param onClick A callback function that is called when the entire preference item is clicked. If no action is needed on click, this can be left empty.
 */
@Composable
fun SwitchPreferenceItemWithDivider(icon : ImageVector? = null , title : String , summary : String , checked : Boolean , onCheckedChange : (Boolean) -> Unit , onClick : () -> Unit , onSwitchClick : (Boolean) -> Unit) {
    val view : View = LocalView.current

    Card(
        modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(size = SizeConstants.ExtraTinySize)) ,
        shape = RoundedCornerShape(size = SizeConstants.ExtraTinySize) ,
    ) {
        Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onClick()
                    }) , verticalAlignment = Alignment.CenterVertically
        ) {
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
                Text(text = title , style = MaterialTheme.typography.titleMedium , fontWeight = FontWeight.Bold , maxLines = 1 , overflow = TextOverflow.Ellipsis)
                Text(text = summary , style = MaterialTheme.typography.bodyMedium)
            }
            ExtraSmallHorizontalSpacer()
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos , contentDescription = null , modifier = Modifier.size(size = SizeConstants.MediumSize) , tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            ExtraSmallHorizontalSpacer()
            VerticalDivider(
                modifier = Modifier
                        .height(height = SizeConstants.MediumSize * 3)
                        .align(alignment = Alignment.CenterVertically) , color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) , thickness = SizeConstants.ExtraTinySize / 2
            )
            CustomSwitch(
                checked = checked,
                onCheckedChange = { isChecked ->
                    onCheckedChange(isChecked)
                    onSwitchClick(isChecked)
                },
                modifier = Modifier.padding(all = SizeConstants.LargeSize)
            )
        }
    }
}