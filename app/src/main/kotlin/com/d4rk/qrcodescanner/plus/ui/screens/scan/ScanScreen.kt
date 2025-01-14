package com.d4rk.qrcodescanner.plus.ui.screens.scan

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.data.datastore.DataStore
import com.d4rk.qrcodescanner.plus.data.model.Barcode
import com.d4rk.qrcodescanner.plus.extension.equalTo
import com.d4rk.qrcodescanner.plus.ui.components.dialogs.ConfirmBarcodeDialog
import com.d4rk.qrcodescanner.plus.usecase.BarcodeDatabase
import com.d4rk.qrcodescanner.plus.usecase.BarcodeParser
import com.d4rk.qrcodescanner.plus.usecase.SupportedBarcodeFormats
import com.d4rk.qrcodescanner.plus.usecase.save
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Composable
fun ScanScreen(
    barcodeParser : BarcodeParser ,
    barcodeDatabase : BarcodeDatabase ,
    navigateToBarcodeScreen : (Barcode) -> Unit ,
    handleScannedData : (String , String , ByteArray?) -> Unit ,
    zxingScanIntentAction : String = "com.google.zxing.client.android.SCAN"
) {
    val context : Context = LocalContext.current
    val view : View = LocalView.current
    val dataStore : DataStore = DataStore.getInstance(context)
    val scope : CoroutineScope = rememberCoroutineScope()
    val activity : Activity = LocalContext.current as Activity

    var showConfirmationDialog : Boolean by remember { mutableStateOf(value = false) }
    var scannedBarcode : Barcode? by remember { mutableStateOf(value = null) }
    var lastResult : Barcode? by remember { mutableStateOf(value = null) }
    val maxZoom : Int by remember { mutableIntStateOf(value = 0) }
    var zoom : Int by remember { mutableIntStateOf(value = 0) }


    // Collect settings from DataStore as state
    val isFlashEnabled : Boolean by dataStore.flash.collectAsState(initial = false)
    val isBackCamera : Boolean by dataStore.isBackCamera.collectAsState(initial = true)
    val simpleAutoFocus : Boolean by dataStore.simpleAutoFocus.collectAsState(initial = false)
    val continuousScanning : Boolean by dataStore.continuousScanning.collectAsState(initial = false)
    val confirmScansManually : Boolean by dataStore.confirmScansManually.collectAsState(initial = false)
    val saveScannedBarcodesToHistory : Boolean by dataStore.saveScannedBarcodesToHistory.collectAsState(
        initial = true
    )
    val doNotSaveDuplicates : Boolean by dataStore.doNotSaveDuplicates.collectAsState(initial = false)

    LaunchedEffect(key1 = Unit) {
        // Example of how to read a format selection from DataStore
        val isQrCodeSelected : Boolean = dataStore.isFormatSelected(BarcodeFormat.QR_CODE).first()
    }

    Card(modifier = Modifier
            .clip(RoundedCornerShape(28.dp))
            .fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {

            if (showConfirmationDialog && scannedBarcode != null) {
                ConfirmBarcodeDialog(barcode = scannedBarcode !! , onConfirm = {
                    handleConfirmedBarcode(scannedBarcode !! ,
                                           barcodeDatabase ,
                                           saveScannedBarcodesToHistory ,
                                           doNotSaveDuplicates ,
                                           onSuccess = { id ->
                                               if (continuousScanning) {
                                                   restartPreviewWithDelay(
                                                       showMessage = true ,
                                                       view ,
                                                       codeScanner = null // You'll need to find a way to access the codeScanner instance here
                                                   )
                                               }
                                               else {
                                                   navigateToBarcodeScreen(scannedBarcode !!.copy(id = id))
                                               }
                                           } ,
                                           onError = {})
                    showConfirmationDialog = false
                } , onDecline = {
                    restartPreviewWithDelay(
                        showMessage = false ,
                        view ,
                        codeScanner = null // You'll need to find a way to access the codeScanner instance here
                    )
                    showConfirmationDialog = false
                })
            }

            Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter) ,  // Align Row at the top center of the Box
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                //    onScanFromFile
                }) {
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(id = R.string.scan_from_file)
                    )
                }

                IconButton(
                    onClick = {
                       // onDecreaseZoom
                    },
                    enabled = zoom > 0
                ) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = stringResource(id = R.string.decrease_zoom)
                    )
                }
            }

            AndroidView(modifier = Modifier
                    .fillMaxSize()
                    , factory = { context ->
                CodeScannerView(context).apply {
                    val codeScanner : CodeScanner = CodeScanner(context , this).apply {
                        camera = if (isBackCamera) {
                            CodeScanner.CAMERA_BACK
                        }
                        else {
                            CodeScanner.CAMERA_FRONT
                        }
                        autoFocusMode = if (simpleAutoFocus) {
                            AutoFocusMode.SAFE
                        }
                        else {
                            AutoFocusMode.CONTINUOUS
                        }

                        formats = SupportedBarcodeFormats.FORMATS.filter { format ->
                            runBlocking {
                                dataStore.isFormatSelected(format).first()
                            }
                        }
                        scanMode = ScanMode.SINGLE
                        isAutoFocusEnabled = true
                        isTouchFocusEnabled = false
                        decodeCallback = DecodeCallback { result ->
                            scope.launch(Dispatchers.Main) {
                                if (activity.intent?.action == zxingScanIntentAction) {
                                    vibrateIfNeeded()
                                    handleScannedData(
                                        result.text,
                                        result.barcodeFormat.toString(),
                                        result.rawBytes
                                    )
                                    return@launch
                                }
                                val codeScanner = this@apply
                                if (continuousScanning && result.equalTo(lastResult)) {
                                    restartPreviewWithDelay(
                                        showMessage = false,
                                        view = view,
                                        codeScanner = codeScanner
                                    )
                                    return@launch
                                }
                                vibrateIfNeeded()
                                val barcode: Barcode = barcodeParser.parseResult(result)
                                when {
                                    confirmScansManually -> {
                                        showConfirmationDialog = true
                                        scannedBarcode = barcode
                                    }
                                    saveScannedBarcodesToHistory || continuousScanning -> {
                                        // Replace RxJava save logic with coroutine
                                        scope.launch {
                                            try {
                                                val id = saveScannedBarcode(barcode, barcodeDatabase, doNotSaveDuplicates)
                                                lastResult = barcode
                                                when (continuousScanning) {
                                                    true -> restartPreviewWithDelay(
                                                        showMessage = true,
                                                        view = view,
                                                        codeScanner = codeScanner
                                                    )
                                                    else -> navigateToBarcodeScreen(
                                                        barcode.copy(id = id)
                                                    )
                                                }
                                            } catch (e: Exception) {
                                                //  showError(e)
                                            }
                                        }
                                    }
                                    else -> navigateToBarcodeScreen(barcode)
                                }
                            }
                        }
                        errorCallback = ErrorCallback {
                            // Show error
                        }
                    }

                    codeScanner.startPreview()

                    codeScanner.zoom = zoom

                    codeScanner.isFlashEnabled = isFlashEnabled
                }
            })

            Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp), // Adjust bottom margin as needed
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Decrease zoom button
                IconButton(onClick = {
                    //onDecreaseZoom
                }, enabled = zoom > 0) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = stringResource(id = R.string.decrease_zoom)
                    )
                }

                Slider(
                    value = zoom.toFloat(),
                    onValueChange = {
                        //newValue -> onZoomChange(newValue.toInt().coerceIn(0, maxZoom))
                    },
                    valueRange = 0f..maxZoom.toFloat(),
                    modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)  // Horizontal padding between slider and buttons
                )

                // Increase zoom button
                IconButton(onClick = {
                    //onIncreaseZoom
                }, enabled = zoom < maxZoom) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.increase_zoom)
                    )
                }
            }
        }
    }
}

private fun vibrateIfNeeded() {
    // TODO: Implement vibrate functionality based on DataStore setting
}

private fun restartPreviewWithDelay(
    showMessage: Boolean, view: View, codeScanner: CodeScanner?
) {
    CoroutineScope(Dispatchers.Main).launch {
        delay(CONTINUOUS_SCANNING_PREVIEW_DELAY)
        if (showMessage) {
            Snackbar.make(view, R.string.saved, Snackbar.LENGTH_LONG).show()
        }
        codeScanner?.let { restartPreview(it) }
    }
}

private fun restartPreview(codeScanner : CodeScanner) {
    codeScanner.startPreview()
}

private suspend fun saveScannedBarcode(
    barcode: Barcode,
    barcodeDatabase: BarcodeDatabase,
    doNotSaveDuplicates: Boolean
): Long {
    return withContext(Dispatchers.IO) {
        barcodeDatabase.save(barcode, doNotSaveDuplicates)
    }
}

private fun handleConfirmedBarcode(
    barcode: Barcode,
    barcodeDatabase: BarcodeDatabase,
    saveScannedBarcodesToHistory: Boolean,
    doNotSaveDuplicates: Boolean,
    onSuccess: (Long) -> Unit,
    onError: (Throwable) -> Unit
) {
    if (saveScannedBarcodesToHistory) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val id = saveScannedBarcode(barcode, barcodeDatabase, doNotSaveDuplicates)
                onSuccess(id)
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }
}


private const val CONTINUOUS_SCANNING_PREVIEW_DELAY = 500L