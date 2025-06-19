package com.d4rk.android.libs.apptoolkit.core.ui.components.dialogs

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(onDateSelected : (String) -> Unit , onDismiss : () -> Unit) {
    val selectedDatePickerState : DatePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val view : View = LocalView.current

    androidx.compose.material3.DatePickerDialog(onDismissRequest = {
        onDismiss()
    } , confirmButton = {
        TextButton(modifier = Modifier.bounceClick(), onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            onDismiss()
            selectedDatePickerState.selectedDateMillis?.let {
                onDateSelected(SimpleDateFormat("yyyy-MM-dd" , Locale.getDefault()).format(Date(it)))
            }
        }) {
            Text(text = stringResource(id = android.R.string.ok))
        }
    } , dismissButton = {
        TextButton(modifier = Modifier.bounceClick(), onClick = {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            onDismiss()
        }) {
            Text(text = stringResource(id = android.R.string.cancel))
        }
    }) {
        DatePicker(state = selectedDatePickerState)
    }
}