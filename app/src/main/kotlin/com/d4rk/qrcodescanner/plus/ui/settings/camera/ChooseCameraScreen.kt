package com.d4rk.qrcodescanner.plus.ui.settings.camera

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.utils.compose.components.SettingsRadioButton

@Composable
fun ChooseCameraScreen(
    isBackCamera : Boolean , onBackCameraChanged : (Boolean) -> Unit
) {
    val scrollState : ScrollState = rememberScrollState()

    Column(
        modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
    ) {
        SettingsRadioButton(
            text = stringResource(R.string.back) ,
            isChecked = isBackCamera ,
            onCheckedChange = onBackCameraChanged
        )

        SettingsRadioButton(
            text = stringResource(R.string.front) ,
            isChecked = ! isBackCamera ,
            onCheckedChange = { isChecked -> onBackCameraChanged(! isChecked) } ,
        )
    }
}