package com.d4rk.qrcodescanner.plus.ui.settings.scanner.formats

import android.view.View
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.extension.toStringId
import com.d4rk.qrcodescanner.plus.usecase.Settings
import com.d4rk.qrcodescanner.plus.usecase.SupportedBarcodeFormats
import com.d4rk.qrcodescanner.plus.utils.compose.components.CheckBoxPreferenceItem
import com.d4rk.qrcodescanner.plus.utils.haptic.weakHapticFeedback
import com.google.zxing.BarcodeFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportedFormatsScreen(activity : SupportedFormatsActivity , settings : Settings) {
    val formats : List<BarcodeFormat> = remember { SupportedBarcodeFormats.FORMATS }
    val formatSelection : MutableList<Boolean> = remember {
        mutableStateListOf(*formats.map { settings.isFormatSelected(it) }.toTypedArray())
    }
    val view : View = LocalView.current
    val scrollBehavior : TopAppBarScrollBehavior =
            TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) , topBar = {
        LargeTopAppBar(title = { Text(stringResource(R.string.supported_formats)) } ,
                       navigationIcon = {
                           IconButton(onClick = {
                               view.weakHapticFeedback()
                               activity.finish()
                           }) {
                               Icon(Icons.AutoMirrored.Filled.ArrowBack , contentDescription = null)
                           }
                       } ,
                       scrollBehavior = scrollBehavior)
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                    .fillMaxHeight()
                    .padding(paddingValues) ,
        ) {
            items(formats.size) { index ->
                val format : BarcodeFormat = formats[index]
                val isChecked : Boolean = formatSelection[index]
                CheckBoxPreferenceItem(title = stringResource(id = format.toStringId()) ,
                                       checked = isChecked ,
                                       onCheckedChange = { newIsChecked ->
                                           settings.setFormatSelected(format , newIsChecked)
                                           formatSelection[index] = newIsChecked
                                       })
            }
        }
    }
}