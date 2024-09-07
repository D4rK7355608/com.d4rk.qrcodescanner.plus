package com.d4rk.qrcodescanner.plus.ui.scan

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.d4rk.qrcodescanner.plus.usecase.Settings
import com.d4rk.qrcodescanner.plus.usecase.SupportedBarcodeFormats
import com.d4rk.qrcodescanner.plus.usecase.save
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Composable
fun ScanScreen(
    settings : Settings ,
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
    var isFlashEnabled : Boolean by remember { mutableStateOf(value = false) }

    LaunchedEffect(key1 = Unit) {
        dataStore.flash.collect { newFlashSetting ->
            isFlashEnabled = newFlashSetting
        }
    }

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
                    camera = if (settings.isBackCamera) {
                        CodeScanner.CAMERA_BACK
                    }
                    else {
                        CodeScanner.CAMERA_FRONT
                    }
                    autoFocusMode = if (settings.simpleAutoFocus) {
                        AutoFocusMode.SAFE
                    }
                    else {
                        AutoFocusMode.CONTINUOUS
                    }
                    formats = SupportedBarcodeFormats.FORMATS.filter(settings::isFormatSelected)
                    scanMode = ScanMode.SINGLE
                    isAutoFocusEnabled = true
                    isTouchFocusEnabled = false
                    decodeCallback = DecodeCallback { result ->
                        scope.launch(Dispatchers.Main) {
                            if (activity.intent?.action == zxingScanIntentAction) {
                                vibrateIfNeeded(settings)
                                handleScannedData(
                                    result.text ,
                                    result.barcodeFormat.toString() ,
                                    result.rawBytes
                                )
                                return@launch
                            }
                            if (settings.continuousScanning && result.equalTo(lastResult)) {
                                restartPreviewWithDelay(
                                    showMessage = false , view , codeScanner = this@apply
                                )
                                return@launch
                            }
                            vibrateIfNeeded(settings)
                            val barcode : Barcode = barcodeParser.parseResult(result)
                            when {
                                settings.confirmScansManually -> {
                                    showConfirmationDialog = true
                                    scannedBarcode = barcode
                                }

                                settings.saveScannedBarcodesToHistory || settings.continuousScanning -> {
                                    saveScannedBarcode(barcode ,
                                                       barcodeDatabase ,
                                                       settings ,
                                                       onSuccess = { id ->
                                                           lastResult = barcode
                                                           when (settings.continuousScanning) {
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
        IconButton(onClick = { isFlashEnabled = ! isFlashEnabled } ,
                   modifier = Modifier.constrainAs(flashButton) {
                       top.linkTo(parent.top , margin = 16.dp)
                       start.linkTo(parent.start , margin = 16.dp)
                   }) {
            Icon(
                imageVector = if (isFlashEnabled) Icons.Filled.FlashOn else Icons.Filled.FlashOff ,
                contentDescription = stringResource(R.string.flash) ,
            )
        }

        IconButton(onClick = {

        } , modifier = Modifier.constrainAs(scanFromFileButton) {
            top.linkTo(parent.top , margin = 16.dp)
            end.linkTo(parent.end , margin = 16.dp)
        }) {
            Icon(
                imageVector = Icons.Filled.Image , contentDescription = null
            )
        }

        IconButton(onClick = {} , modifier = Modifier.constrainAs(decreaseZoomButton) {
            bottom.linkTo(parent.bottom , margin = 16.dp)
            start.linkTo(parent.start , margin = 16.dp)
        } , enabled = zoom > 0) {
            Icon(
                imageVector = Icons.Filled.Remove ,
                contentDescription = null ,
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

        IconButton(onClick = {

        } , modifier = Modifier.constrainAs(increaseZoomButton) {
            bottom.linkTo(parent.bottom , margin = 16.dp)
            end.linkTo(parent.end , margin = 16.dp)
        } , enabled = zoom < maxZoom) {
            Icon(
                imageVector = Icons.Filled.Add , contentDescription = null
            )
        }

        if (showConfirmationDialog && scannedBarcode != null) {
            ConfirmBarcodeDialog(barcode = scannedBarcode !! , onConfirm = {
                handleConfirmedBarcode(scannedBarcode !! ,
                                       settings ,
                                       barcodeDatabase ,
                                       onSuccess = { id ->
                                           if (settings.continuousScanning) {
                                           }
                                           else {
                                               navigateToBarcodeScreen(scannedBarcode !!.copy(id = id))
                                           }
                                       } ,
                                       onError = {})
                showConfirmationDialog = false
            } , onDecline = {
                showConfirmationDialog = false
            } , modifier = Modifier.constrainAs(confirmationDialog) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
        }

    }
}

private fun vibrateIfNeeded(settings : Settings) {
}

private fun restartPreviewWithDelay(
    showMessage : Boolean , view : View , codeScanner : CodeScanner
) {
    Completable.timer(CONTINUOUS_SCANNING_PREVIEW_DELAY , TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (showMessage) {
                    Snackbar.make(view , R.string.saved , Snackbar.LENGTH_LONG).show()
                }
                restartPreview(codeScanner)
            }.addTo(disposable)
}

private fun restartPreview(codeScanner : CodeScanner) {
    codeScanner.startPreview()
}

private fun saveScannedBarcode(
    barcode : Barcode ,
    barcodeDatabase : BarcodeDatabase ,
    settings : Settings ,
    onSuccess : (Long) -> Unit ,
    onError : (Throwable) -> Unit
) {
    barcodeDatabase.save(barcode , settings.doNotSaveDuplicates).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccess , onError)
            .addTo(disposable)
}

private fun handleConfirmedBarcode(
    barcode : Barcode ,
    settings : Settings ,
    barcodeDatabase : BarcodeDatabase ,
    onSuccess : (Long) -> Unit ,
    onError : (Throwable) -> Unit
) {
    when {
        settings.saveScannedBarcodesToHistory || settings.continuousScanning -> saveScannedBarcode(
            barcode , barcodeDatabase , settings , onSuccess = onSuccess , onError = onError
        )

        else -> null
    }
}

private val disposable = CompositeDisposable()
private const val CONTINUOUS_SCANNING_PREVIEW_DELAY = 500L