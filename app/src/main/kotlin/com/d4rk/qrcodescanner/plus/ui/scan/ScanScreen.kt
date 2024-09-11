package com.d4rk.qrcodescanner.plus.ui.scan

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
import com.d4rk.qrcodescanner.plus.ui.dialogs.ConfirmBarcodeDialog
import com.d4rk.qrcodescanner.plus.usecase.BarcodeDatabase
import com.d4rk.qrcodescanner.plus.usecase.BarcodeParser
import com.d4rk.qrcodescanner.plus.usecase.SupportedBarcodeFormats
import com.d4rk.qrcodescanner.plus.usecase.save
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

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

    Card(modifier = Modifier.clip(RoundedCornerShape(36.dp))) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (scanner : ConstrainedLayoutReference , flashButton : ConstrainedLayoutReference , scanFromFileButton : ConstrainedLayoutReference , decreaseZoomButton : ConstrainedLayoutReference , zoomSeekBar : ConstrainedLayoutReference , increaseZoomButton : ConstrainedLayoutReference , confirmationDialog : ConstrainedLayoutReference) = createRefs()
            AndroidView(modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(scanner) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    } , factory = { context ->
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
                                        result.text ,
                                        result.barcodeFormat.toString() ,
                                        result.rawBytes
                                    )
                                    return@launch
                                }
                                if (continuousScanning && result.equalTo(lastResult)) {
                                    restartPreviewWithDelay(
                                        showMessage = false , view , codeScanner = this@apply
                                    )
                                    return@launch
                                }
                                vibrateIfNeeded()
                                val barcode : Barcode = barcodeParser.parseResult(result)
                                when {
                                    confirmScansManually -> {
                                        showConfirmationDialog = true
                                        scannedBarcode = barcode
                                    }

                                    saveScannedBarcodesToHistory || continuousScanning -> {
                                        saveScannedBarcode(barcode ,
                                                           barcodeDatabase ,
                                                           doNotSaveDuplicates , // FIXME: Type mismatch: inferred type is Boolean but Settings was expected
                                                           onSuccess = { id ->
                                                               lastResult = barcode
                                                               when (continuousScanning) {
                                                                   true -> restartPreviewWithDelay(
                                                                       showMessage = true ,
                                                                       view ,
                                                                       codeScanner = this@apply
                                                                   )

                                                                   else -> navigateToBarcodeScreen(
                                                                       barcode.copy(id = id)
                                                                   )
                                                               }
                                                           } ,
                                                           onError = {
                                                               // Show error
                                                           })
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

            IconButton(onClick = {
                // TODO: Implement scan from file functionality
            } , modifier = Modifier.constrainAs(scanFromFileButton) {
                top.linkTo(parent.top , margin = 16.dp)
                end.linkTo(parent.end , margin = 16.dp)
            }) {
                Icon(
                    imageVector = Icons.Filled.Image ,
                    contentDescription = stringResource(id = R.string.scan_from_file)
                )
            }

            IconButton(onClick = { zoom = (zoom - 1).coerceAtLeast(0) } ,
                       modifier = Modifier.constrainAs(decreaseZoomButton) {
                           bottom.linkTo(parent.bottom , margin = 16.dp)
                           start.linkTo(parent.start , margin = 16.dp)
                       } ,
                       enabled = zoom > 0
            ) {
                Icon(
                    imageVector = Icons.Filled.Remove ,
                    contentDescription = stringResource(id = R.string.decrease_zoom) ,
                )
            }

            Slider(
                value = zoom.toFloat() ,
                onValueChange = { newZoom ->
                    zoom = newZoom.toInt().coerceIn(0 , maxZoom)
                } ,
                valueRange = 0f..maxZoom.toFloat() ,
                modifier = Modifier.constrainAs(zoomSeekBar) {
                    bottom.linkTo(parent.bottom , margin = 16.dp)
                    start.linkTo(decreaseZoomButton.end , margin = 8.dp)
                    end.linkTo(increaseZoomButton.start , margin = 8.dp)
                    width = Dimension.preferredWrapContent
                } ,
            )

            IconButton(onClick = { zoom = (zoom + 1).coerceAtMost(maxZoom) } ,
                       modifier = Modifier.constrainAs(increaseZoomButton) {
                           bottom.linkTo(parent.bottom , margin = 16.dp)
                           end.linkTo(parent.end , margin = 16.dp)
                       } ,
                       enabled = zoom < maxZoom
            ) {
                Icon(
                    imageVector = Icons.Filled.Add ,
                    contentDescription = stringResource(id = R.string.increase_zoom) ,
                )
            }

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
                } , modifier = Modifier.constrainAs(confirmationDialog) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })
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
    Completable.timer(CONTINUOUS_SCANNING_PREVIEW_DELAY , TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (showMessage) {
                    Snackbar.make(view , R.string.saved , Snackbar.LENGTH_LONG).show()
                }
                if (codeScanner != null) {
                    restartPreview(codeScanner)
                }
            }.addTo(disposable)
}

private fun restartPreview(codeScanner : CodeScanner) {
    codeScanner.startPreview()
}

private fun saveScannedBarcode(
    barcode : Barcode ,
    barcodeDatabase : BarcodeDatabase ,
    doNotSaveDuplicates : Boolean ,
    onSuccess : (Long) -> Unit ,
    onError : (Throwable) -> Unit
) {
    barcodeDatabase.save(barcode , doNotSaveDuplicates).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccess , onError)
            .addTo(disposable)
}

private fun handleConfirmedBarcode(
    barcode : Barcode ,
    barcodeDatabase : BarcodeDatabase ,
    saveScannedBarcodesToHistory : Boolean ,
    doNotSaveDuplicates : Boolean ,
    onSuccess : (Long) -> Unit ,
    onError : (Throwable) -> Unit
) {
    when {
        saveScannedBarcodesToHistory -> saveScannedBarcode(
            barcode ,
            barcodeDatabase ,
            doNotSaveDuplicates ,
            onSuccess = onSuccess ,
            onError = onError
        )

        else -> null
    }
}

private val disposable = CompositeDisposable()
private const val CONTINUOUS_SCANNING_PREVIEW_DELAY = 500L