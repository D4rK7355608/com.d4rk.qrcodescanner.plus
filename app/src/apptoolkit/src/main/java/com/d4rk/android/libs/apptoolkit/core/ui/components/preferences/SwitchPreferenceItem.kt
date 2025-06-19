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
import com.d4rk.android.libs.apptoolkit.core.ui.components.switches.CustomSwitch
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * Creates a clickable preference item with a switch for app preference screens.
 *
 * This composable function combines an optional icon, title, optional summary, and a switch into a single row.
 * The entire row is clickable and toggles the switch when clicked, calling the provided `onCheckedChange` callback function with the new state.
 *
 * @param icon An optional icon to be displayed at the start of the preference item. If provided, it should be an `ImageVector` object.
 * @param title The main title text displayed for the preference item.
 * @param summary An optional secondary text displayed below the title for additional information about the preference.
 * @param checked The initial state of the switch. Set to true for on and false for off.
 * @param onCheckedChange A callback function that is called whenever the switch is toggled. This function receives the new state of the switch (boolean) as a parameter.
 */
@Composable
fun SwitchPreferenceItem(icon : ImageVector? = null , title : String , summary : String? = null , checked : Boolean , onCheckedChange : (Boolean) -> Unit) {
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
                        onCheckedChange(! checked)
                    }) ,
            verticalAlignment = Alignment.CenterVertically ,
        ) {
            icon?.let {
                LargeHorizontalSpacer()
                Icon(imageVector = it , contentDescription = null)
                LargeHorizontalSpacer()
            }
            Column(
                modifier = Modifier
                        .padding(all = SizeConstants.LargeSize)
                        .weight(weight = 1f) ,
            ) {
                Text(text = title , style = MaterialTheme.typography.titleMedium , fontWeight = FontWeight.Bold , maxLines = 1 , overflow = TextOverflow.Ellipsis)
                summary?.let {
                    Text(text = it , style = MaterialTheme.typography.bodyMedium)
                }
            }
            CustomSwitch(
                checked = checked,
                onCheckedChange = { isChecked ->
                    onCheckedChange(isChecked)
                },
                modifier = Modifier.padding(all = SizeConstants.LargeSize)
            )
        }
    }
}