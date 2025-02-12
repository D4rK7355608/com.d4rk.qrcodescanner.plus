package com.d4rk.qrcodescanner.plus.ui.screens.scan.screens.qrpreview

import android.app.Activity
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
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
    hostActivity: Activity,
    barcodeDataStore: DataStore,
    scannerView: View,
    enableFlash: Boolean,
    useBackCamera: Boolean,
    useSimpleAutoFocus: Boolean,
    scanContinuously: Boolean,
    requireConfirmation: Boolean,
    saveToHistory: Boolean,
    skipDuplicates: Boolean,
    initialMaxZoom: Int = 100,
    barcodeDecoder: BarcodeParser,
    barcodeRepository: BarcodeDatabase,
    navigateToDetailsScreen: (Barcode) -> Unit,
    handleExternalScanResult: (String, String, ByteArray?) -> Unit,
    externalScanIntentAction: String = "com.google.zxing.client.android.SCAN"
) {
    var isConfirmationDialogVisible by remember { mutableStateOf(false) }
    var barcodeToConfirm: Barcode? by remember { mutableStateOf(null) }
    var previousResult: Barcode? by remember { mutableStateOf(null) }
    val scope: CoroutineScope = rememberCoroutineScope()

    var codeScannerInstance by remember { mutableStateOf<CodeScanner?>(null) }
    val maxZoom by remember { mutableIntStateOf(initialMaxZoom) }
    var zoom by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isConfirmationDialogVisible && barcodeToConfirm != null) {
            ConfirmBarcodeDialog(
                barcode = barcodeToConfirm!!,
                onConfirm = {
                    handleConfirmedBarcode(
                        barcodeToConfirm!!,
                        barcodeRepository,
                        saveToHistory,
                        skipDuplicates,
                        onSuccess = { barcodeId ->
                            if (scanContinuously) {
                                restartPreviewWithDelay(
                                    showMessage = true,
                                    view = scannerView,
                                    codeScanner = codeScannerInstance
                                )
                            } else {
                                navigateToDetailsScreen(barcodeToConfirm!!.copy(id = barcodeId))
                            }
                        },
                        onError = { }
                    )
                    isConfirmationDialogVisible = false
                },
                onDecline = {
                    restartPreviewWithDelay(
                        showMessage = false,
                        view = scannerView,
                        codeScanner = codeScannerInstance
                    )
                    isConfirmationDialogVisible = false
                }
            )
        }

        Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .zIndex(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { }, enabled = zoom > 0) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = stringResource(id = R.string.decrease_zoom)
                )
            }

            Slider(
                value = zoom.toFloat(),
                onValueChange = { newValue ->
                    val newZoom = newValue.toInt().coerceIn(0, maxZoom)
                    zoom = newZoom
                    codeScannerInstance?.zoom = newZoom
                },
                valueRange = 0f..maxZoom.toFloat(),
                modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
            )

            IconButton(
                onClick = {
                    val newZoom = (zoom + 5).coerceAtMost(maxZoom)
                    zoom = newZoom
                    codeScannerInstance?.zoom = newZoom
                },
                enabled = zoom < maxZoom
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.increase_zoom)
                )
            }
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                CodeScannerView(context).apply {
                    val scanner = CodeScanner(context, this).apply {
                        camera = if (useBackCamera) CodeScanner.CAMERA_BACK else CodeScanner.CAMERA_FRONT
                        autoFocusMode = if (useSimpleAutoFocus) AutoFocusMode.SAFE else AutoFocusMode.CONTINUOUS
                        formats = SupportedBarcodeFormats.FORMATS.filter { barcodeFormat ->
                            runBlocking { barcodeDataStore.isFormatSelected(format = barcodeFormat).first() }
                        }
                        scanMode = ScanMode.SINGLE
                        isAutoFocusEnabled = true
                        isTouchFocusEnabled = false

                        decodeCallback = DecodeCallback { result ->
                            val barcodeScanner = this@apply
                            scope.launch(Dispatchers.Main) {
                                if (hostActivity.intent?.action == externalScanIntentAction) {
                                    handleExternalScanResult(
                                        result.text,
                                        result.barcodeFormat.toString(),
                                        result.rawBytes
                                    )
                                    return@launch
                                }
                                if (scanContinuously && result.equalTo(previousResult)) {
                                    restartPreviewWithDelay(
                                        showMessage = false,
                                        view = scannerView,
                                        codeScanner = barcodeScanner
                                    )
                                    return@launch
                                }
                                val barcode: Barcode = barcodeDecoder.parseResult(result)
                                when {
                                    requireConfirmation -> {
                                        isConfirmationDialogVisible = true
                                        barcodeToConfirm = barcode
                                    }
                                    saveToHistory || scanContinuously -> {
                                        scope.launch {
                                            try {
                                                val barcodeId = saveScannedBarcode(
                                                    barcode,
                                                    barcodeRepository,
                                                    skipDuplicates
                                                )
                                                previousResult = barcode
                                                if (scanContinuously) {
                                                    restartPreviewWithDelay(
                                                        showMessage = true,
                                                        view = scannerView,
                                                        codeScanner = barcodeScanner
                                                    )
                                                } else {
                                                    navigateToDetailsScreen(barcode.copy(id = barcodeId))
                                                }
                                            } catch (scanError: Exception) { }
                                        }
                                    }
                                    else -> navigateToDetailsScreen(barcode)
                                }
                            }
                        }
                        errorCallback = ErrorCallback { }
                    }
                    scanner.startPreview()
                    scanner.zoom = zoom
                    scanner.isFlashEnabled = enableFlash

                    codeScannerInstance = scanner
                    this
                }
            }
        )
    }
}

private fun restartPreviewWithDelay(showMessage: Boolean, view: View, codeScanner: CodeScanner?) {
    CoroutineScope(Dispatchers.Main).launch {
        delay(500L)
        if (showMessage) {
            Snackbar.make(view, R.string.saved, Snackbar.LENGTH_LONG).show()
        }
        codeScanner?.startPreview()
    }
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
    saveToHistory: Boolean,
    doNotSaveDuplicates: Boolean,
    onSuccess: (Long) -> Unit,
    onError: (Throwable) -> Unit
) {
    if (saveToHistory) {
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
