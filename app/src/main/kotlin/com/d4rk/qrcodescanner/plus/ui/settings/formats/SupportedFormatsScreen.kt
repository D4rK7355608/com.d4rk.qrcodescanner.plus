package com.d4rk.qrcodescanner.plus.ui.settings.formats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.d4rk.qrcodescanner.plus.extension.toStringId
import com.d4rk.qrcodescanner.plus.usecase.Settings
import com.d4rk.qrcodescanner.plus.usecase.SupportedBarcodeFormats
import com.google.zxing.BarcodeFormat

@Composable
fun SupportedFormatsScreen(settings : Settings) {
    val formats : List<BarcodeFormat> = remember { SupportedBarcodeFormats.FORMATS }
    val formatSelection : MutableList<Boolean> = remember {
        mutableStateListOf(*formats.map { settings.isFormatSelected(it) }.toTypedArray())
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(formats.size) { index ->
            val format : BarcodeFormat = formats[index]
            val isChecked : Boolean = formatSelection[index]
            FormatItem(
                format = format ,
                isChecked = isChecked ,
                onCheckedChange = { newIsChecked ->
                    settings.setFormatSelected(format , newIsChecked)
                    formatSelection[index] = newIsChecked
                } ,
            )
        }
    }
}

@Composable
fun FormatItem(
    format : BarcodeFormat ,
    isChecked : Boolean ,
    onCheckedChange : (Boolean) -> Unit ,
) {
    Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(! isChecked) }
            .padding(vertical = 8.dp) , verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = format.toStringId()) ,
            modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
        )
        Checkbox(
            checked = isChecked ,
            onCheckedChange = onCheckedChange ,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}