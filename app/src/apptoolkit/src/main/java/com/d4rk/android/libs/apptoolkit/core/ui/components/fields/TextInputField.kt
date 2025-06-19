package com.d4rk.android.libs.apptoolkit.core.ui.components.fields

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick

@Composable
fun DropdownMenuBox(selectedText : String , options : List<String> , onOptionSelected : (String) -> Unit) {
    var expanded : Boolean by remember { mutableStateOf(value = false) }
    val view : View = LocalView.current
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = selectedText , onValueChange = {} , modifier = Modifier
                .fillMaxWidth()
                .bounceClick()
                .clickable {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    expanded = true
                } , readOnly = true , trailingIcon = { Icon(imageVector = Icons.Default.ArrowDropDown , contentDescription = null) })
        DropdownMenu(expanded = expanded , onDismissRequest = { expanded = false }) {
            options.forEach { option : String ->
                DropdownMenuItem(text = { Text(text = option) } , onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}