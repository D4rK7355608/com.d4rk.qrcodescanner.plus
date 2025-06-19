package com.d4rk.android.libs.apptoolkit.core.ui.components.preferences

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * A composable function that creates a radio button preference item.
 *
 * This item displays a text label and a radio button. Clicking on the item toggles the radio button's state.
 *
 * @param text The text to display next to the radio button.
 * @param isChecked Whether the radio button is currently checked.
 * @param onCheckedChange A callback that is invoked when the radio button's state changes.
 *                        It provides the new checked state as a Boolean parameter.
 */
@Composable
fun RadioButtonPreferenceItem(text : String , isChecked : Boolean , onCheckedChange : (Boolean) -> Unit) {
    val view : View = LocalView.current
    Row(modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onCheckedChange(! isChecked)
            } , verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text , style = MaterialTheme.typography.titleLarge , modifier = Modifier
                    .weight(weight = 1f)
                    .padding(end = SizeConstants.LargeSize , start = SizeConstants.LargeSize)
        )
        RadioButton(selected = isChecked , onClick = { onCheckedChange(! isChecked) })
    }
}