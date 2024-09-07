package com.d4rk.qrcodescanner.plus.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.data.model.Barcode

@Composable
fun ConfirmBarcodeDialog(
    barcode: Barcode ,
    onConfirm: () -> Unit ,
    onDecline: () -> Unit ,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDecline,
        title = {
            Text(text = "Ceva")
        },
        text = {
            ConfirmBarcodeDialogContent(barcode)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDecline) {
                Text(stringResource(android.R.string.cancel))
            }
        },
        modifier = modifier
    )
}

@Composable
fun ConfirmBarcodeDialogContent(barcode: Barcode) {
    Column {
        // Display relevant barcode information here, e.g.,
        Text(text = "Type: ${barcode.format}")
        Text(text = "Data: ${barcode.text}")
        // Add more fields as needed
    }
}