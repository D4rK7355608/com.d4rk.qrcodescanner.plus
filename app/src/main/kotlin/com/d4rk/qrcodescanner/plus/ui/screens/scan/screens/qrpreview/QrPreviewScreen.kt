package com.d4rk.qrcodescanner.plus.ui.screens.scan.screens.qrpreview

import android.app.Activity
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.d4rk.qrcodescanner.plus.data.database.BarcodeDatabase
import com.d4rk.qrcodescanner.plus.data.database.save
import com.d4rk.qrcodescanner.plus.data.datastore.DataStore
import com.d4rk.qrcodescanner.plus.data.model.Barcode
import com.d4rk.qrcodescanner.plus.ui.components.dialogs.ConfirmBarcodeDialog
import com.d4rk.qrcodescanner.plus.usecase.BarcodeParser
import com.d4rk.qrcodescanner.plus.usecase.SupportedBarcodeFormats
import com.d4rk.qrcodescanner.plus.utils.extensions.equalTo
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Composable
fun QrPreviewScreen(
    hostActivity : Activity ,
    barcodeDataStore : DataStore ,
    scannerView : View ,
    enableFlash : Boolean ,
    useBackCamera : Boolean ,
    useSimpleAutoFocus : Boolean ,
    scanContinuously : Boolean ,
    requireConfirmation : Boolean ,
    saveToHistory : Boolean ,
    skipDuplicates : Boolean ,
    maximumZoom : Int ,
    zoom : Int ,
    barcodeDecoder : BarcodeParser ,
    barcodeRepository : BarcodeDatabase ,
    navigateToDetailsScreen : (Barcode) -> Unit ,
    handleExternalScanResult : (String , String , ByteArray?) -> Unit ,
    externalScanIntentAction : String = "com.google.zxing.client.android.SCAN"
) {
    var isConfirmationDialogVisible : Boolean by remember { mutableStateOf(value = false) }
    var barcodeToConfirm : Barcode? by remember { mutableStateOf(value = null) }
    var previousResult : Barcode? by remember { mutableStateOf(value = null) }
    val scope : CoroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        if (isConfirmationDialogVisible && barcodeToConfirm != null) {
            ConfirmBarcodeDialog(barcode = barcodeToConfirm !! , onConfirm = {
                handleConfirmedBarcode(barcodeToConfirm !! ,
                                       barcodeRepository ,
                                       saveToHistory ,
                                       skipDuplicates ,
                                       onSuccess = { barcodeId ->
                                           if (scanContinuously) {
                                               restartPreviewWithDelay(
                                                   showMessage = true , scannerView , codeScanner = null
                                               )
                                           }
                                           else {
                                               navigateToDetailsScreen(barcodeToConfirm !!.copy(id = barcodeId))
                                           }
                                       } ,
                                       onError = {})
                isConfirmationDialogVisible = false
            } , onDecline = {
                restartPreviewWithDelay(
                    showMessage = false , view = scannerView , codeScanner = null
                )
                isConfirmationDialogVisible = false
            })
        }

        Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp) , horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    // onScanFromFile
                }) {
                    Icon(
                        imageVector = Icons.Filled.Image ,
                        contentDescription = stringResource(id = R.string.scan_from_file)
                    )
                }
                Text(text = stringResource(id = R.string.scan_image))
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    // onDecreaseZoom
                } , enabled = zoom > 0) {
                    Icon(
                        imageVector = Icons.Filled.Remove ,
                        contentDescription = stringResource(id = R.string.decrease_zoom)
                    )
                }
                Text(text = stringResource(id = R.string.flash))
            }
        }

        AndroidView(modifier = Modifier.fillMaxSize() , factory = { context ->
            CodeScannerView(context).apply {
                val barcodeScanner : CodeScanner = CodeScanner(context , this).apply {
                    camera = if (useBackCamera) {
                        CodeScanner.CAMERA_BACK
                    }
                    else {
                        CodeScanner.CAMERA_FRONT
                    }
                    autoFocusMode = if (useSimpleAutoFocus) {
                        AutoFocusMode.SAFE
                    }
                    else {
                        AutoFocusMode.CONTINUOUS
                    }

                    formats = SupportedBarcodeFormats.FORMATS.filter { barcodeFormat ->
                        runBlocking {
                            barcodeDataStore.isFormatSelected(format = barcodeFormat).first()
                        }
                    }
                    scanMode = ScanMode.SINGLE
                    isAutoFocusEnabled = true
                    isTouchFocusEnabled = false
                    decodeCallback = DecodeCallback { result ->
                        scope.launch(Dispatchers.Main) {
                            if (hostActivity.intent?.action == externalScanIntentAction) {
                                vibrateIfNeeded()
                                handleExternalScanResult(
                                    result.text , result.barcodeFormat.toString() , result.rawBytes
                                )
                                return@launch
                            }
                            val barcodeScanner = this@apply
                            if (scanContinuously && result.equalTo(previousResult)) {
                                restartPreviewWithDelay(
                                    showMessage = false , view = scannerView , codeScanner = barcodeScanner
                                )
                                return@launch
                            }
                            vibrateIfNeeded()
                            val barcode : Barcode = barcodeDecoder.parseResult(result)
                            when {
                                requireConfirmation -> {
                                    isConfirmationDialogVisible = true
                                    barcodeToConfirm = barcode
                                }

                                saveToHistory || scanContinuously -> {
                                    scope.launch {
                                        try {
                                            val barcodeId = saveScannedBarcode(
                                                barcode , barcodeRepository , skipDuplicates
                                            )
                                            previousResult = barcode
                                            when (scanContinuously) {
                                                true -> restartPreviewWithDelay(
                                                    showMessage = true ,
                                                    view = scannerView ,
                                                    codeScanner = barcodeScanner
                                                )

                                                else -> navigateToDetailsScreen(
                                                    barcode.copy(id = barcodeId)
                                                )
                                            }
                                        } catch (scanError : Exception) {
                                            //  showError(scanError)
                                        }
                                    }
                                }

                                else -> navigateToDetailsScreen(barcode)
                            }
                        }
                    }
                    errorCallback = ErrorCallback {
                        // Show error
                    }
                }

                barcodeScanner.startPreview()

                barcodeScanner.zoom = zoom

                barcodeScanner.isFlashEnabled = enableFlash
            }
        })

        Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp) ,
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {
            } , enabled = zoom > 0) {
                Icon(
                    imageVector = Icons.Filled.Remove ,
                    contentDescription = stringResource(id = R.string.decrease_zoom)
                )
            }

            Slider(value = zoom.toFloat() ,
                   onValueChange = {
                       //newValue -> onZoomChange(newValue.toInt().coerceIn(0, maximumZoom))
                   } ,
                   valueRange = 0f..maximumZoom.toFloat() ,
                   modifier = Modifier
                           .weight(1f)
                           .padding(horizontal = 8.dp)  // Horizontal padding between slider and buttons
            )

            IconButton(onClick = {
                //onIncreaseZoom
            } , enabled = zoom < maximumZoom) {
                Icon(
                    imageVector = Icons.Filled.Add ,
                    contentDescription = stringResource(id = R.string.increase_zoom)
                )
            }
        }
    }
}


private fun vibrateIfNeeded() {
    // TODO: Implement vibrate functionality based on DataStore setting
}

private fun restartPreviewWithDelay(
    showMessage : Boolean , view : View , codeScanner : CodeScanner?
) {
    CoroutineScope(Dispatchers.Main).launch {
        delay(CONTINUOUS_SCANNING_PREVIEW_DELAY)
        if (showMessage) {
            Snackbar.make(view , R.string.saved , Snackbar.LENGTH_LONG).show()
        }
        codeScanner?.let { restartPreview(it) }
    }
}

private fun restartPreview(codeScanner : CodeScanner) {
    codeScanner.startPreview()
}

private suspend fun saveScannedBarcode(
    barcode : Barcode , barcodeDatabase : BarcodeDatabase , doNotSaveDuplicates : Boolean
) : Long {
    return withContext(Dispatchers.IO) {
        barcodeDatabase.save(barcode , doNotSaveDuplicates)
    }
}

private fun handleConfirmedBarcode(
    barcode : Barcode ,
    barcodeDatabase : BarcodeDatabase ,
    saveScannedBarcodesToHistory : Boolean ,
    doNotSaveDuplicates : Boolean ,
    onSuccess : (Long) -> Unit ,
    onError : (Throwable) -> Unit
) {
    if (saveScannedBarcodesToHistory) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val id = saveScannedBarcode(barcode , barcodeDatabase , doNotSaveDuplicates)
                onSuccess(id)
            } catch (e : Throwable) {
                onError(e)
            }
        }
    }
}


private const val CONTINUOUS_SCANNING_PREVIEW_DELAY = 500L