package com.d4rk.android.libs.apptoolkit.core.ui.components.fields

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.ui.components.dialogs.DatePickerDialog
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerTextField(date : Date , onDateSelected : (Date) -> Unit) {
    val formatter : SimpleDateFormat = remember { SimpleDateFormat("dd.MM.yyyy" , Locale.getDefault()) }
    var showDialog : Boolean by remember { mutableStateOf<Boolean>(value = false) }
    val view : View = LocalView.current
    if (showDialog) {
        DatePickerDialog(onDateSelected = { dateString : String ->
            val parsed : Date = SimpleDateFormat("yyyy-MM-dd" , Locale.getDefault()).parse(dateString) ?: date
            onDateSelected(parsed)
            showDialog = false
        } , onDismiss = { showDialog = false })
    }
    OutlinedTextField(value = formatter.format(date) , onValueChange = {} , readOnly = true , enabled = false , modifier = Modifier
            .fillMaxWidth()
            .bounceClick()
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                showDialog = true
            } , trailingIcon = {
        Icon(imageVector = Icons.Default.CalendarToday , contentDescription = null)
    } , colors = OutlinedTextFieldDefaults.colors(
        disabledTextColor = MaterialTheme.colorScheme.onSurface , disabledBorderColor = MaterialTheme.colorScheme.outline , disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant , disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
    ))
}