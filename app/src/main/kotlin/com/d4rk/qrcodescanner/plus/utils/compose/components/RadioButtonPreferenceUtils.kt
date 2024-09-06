package com.d4rk.qrcodescanner.plus.utils.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsRadioButton(
    text : String ,
    isChecked : Boolean ,
    onCheckedChange : (Boolean) -> Unit ,
) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onCheckedChange(! isChecked) }
            .padding(vertical = 8.dp) , verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text , modifier = Modifier.weight(1f).padding(end = 16.dp)
        )
        RadioButton(selected = isChecked , onClick = { onCheckedChange(! isChecked) })
    }
}