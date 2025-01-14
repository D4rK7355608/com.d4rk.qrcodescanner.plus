package com.d4rk.qrcodescanner.plus.ui.screens.settings.scanner.formats

import android.content.Context
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.data.datastore.DataStore
import com.d4rk.qrcodescanner.plus.extension.toStringId
import com.d4rk.qrcodescanner.plus.usecase.SupportedBarcodeFormats
import com.d4rk.qrcodescanner.plus.utils.compose.components.CheckBoxPreferenceItem
import com.d4rk.qrcodescanner.plus.utils.haptic.weakHapticFeedback
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportedFormatsScreen(activity: SupportedFormatsActivity) {
    val context: Context = LocalContext.current
    val view: View = LocalView.current
    val dataStore: DataStore = DataStore.getInstance(context)
    val scope: CoroutineScope = rememberCoroutineScope()

    val formats: List<BarcodeFormat> = remember { SupportedBarcodeFormats.FORMATS }

    val scrollBehavior: TopAppBarScrollBehavior =
            TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        LargeTopAppBar(title = { Text(stringResource(R.string.supported_formats)) },
                       navigationIcon = {
                           IconButton(onClick = {
                               view.weakHapticFeedback()
                               activity.finish()
                           }) {
                               Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                           }
                       },
                       scrollBehavior = scrollBehavior)
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                    .fillMaxHeight()
                    .padding(paddingValues)
        ) {
            items(formats.size) { index ->
                val format: BarcodeFormat = formats[index]
                val isChecked : Boolean by dataStore.isFormatSelected(format).collectAsState(initial = true)

                CheckBoxPreferenceItem(
                    title = stringResource(id = format.toStringId()),
                    checked = isChecked
                ) { newIsChecked ->
                    scope.launch {
                        dataStore.setFormatSelected(format, newIsChecked)
                    }
                }
            }
        }
    }
}