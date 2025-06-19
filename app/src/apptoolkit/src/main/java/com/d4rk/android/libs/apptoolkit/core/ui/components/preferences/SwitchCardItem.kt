package com.d4rk.android.libs.apptoolkit.core.ui.components.preferences

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.d4rk.android.libs.apptoolkit.core.ui.components.switches.CustomSwitch
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * Creates a clickable card with a title and a switch for app preference screens.
 *
 * This composable function displays a card with a title and a switch. The entire card is clickable, and clicking it toggles the switch and invokes the `onSwitchToggled` callback.
 * The switch visually indicates its 'on' state by displaying a check icon within the thumb.
 *
 * @param title The text displayed as the card's title.
 * @param switchState A [State] object holding the current on/off state of the switch. Use `true` for the 'on' state and `false` for the 'off' state.
 * @param onSwitchToggled A callback function invoked when the switch is toggled, either by clicking the card or the switch itself.  It receives the new state of the switch (a `Boolean` value) as a parameter.
 *
 * The card has a rounded corner shape and provides a click sound effect upon interaction.
 */
@Composable
fun SwitchCardItem(title : String , switchState : State<Boolean> , onSwitchToggled : (Boolean) -> Unit) {
    val view : View = LocalView.current
    Card(
        shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize) ,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer) ,
        modifier = Modifier
                .fillMaxWidth()
                .padding(all = SizeConstants.MediumSize * 2)
                .clip(shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize))
                .clickable {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    onSwitchToggled(! switchState.value)
                }) {
        Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = SizeConstants.LargeSize) , horizontalArrangement = Arrangement.SpaceBetween , verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title , maxLines = 1 , overflow = TextOverflow.Ellipsis , fontWeight = FontWeight.Bold)
            CustomSwitch(
                checked = switchState.value,
                onCheckedChange = { isChecked ->
                    onSwitchToggled(isChecked)
                },
            )
        }
    }
}