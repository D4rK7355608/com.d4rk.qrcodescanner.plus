package com.d4rk.qrcodescanner.plus.ui.screens.scan

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.d4rk.qrcodescanner.plus.data.core.AppCoreManager
import com.d4rk.qrcodescanner.plus.data.database.BarcodeDatabase
import com.d4rk.qrcodescanner.plus.data.datastore.DataStore
import com.d4rk.qrcodescanner.plus.data.model.Barcode
import com.d4rk.qrcodescanner.plus.ui.screens.scan.screens.nocamerapermission.NoCameraPermissionScreen
import com.d4rk.qrcodescanner.plus.ui.screens.scan.screens.qrpreview.QrPreviewScreen
import com.d4rk.qrcodescanner.plus.usecase.BarcodeParser
import com.d4rk.qrcodescanner.plus.utils.helpers.AppPermissionsManager

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
    val dataStore : DataStore = AppCoreManager.dataStore
    val activity : Activity = LocalContext.current as Activity
    var hasCameraPermission by remember { mutableStateOf(AppPermissionsManager.hasCameraPermission(context)) }
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
        snapshotFlow { AppPermissionsManager.hasCameraPermission(context) }
                .collect { granted ->
                    hasCameraPermission = granted
                }
    }

    Card(
        modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .fillMaxSize()
    ) {

        if (hasCameraPermission) {
            QrPreviewScreen(
                hostActivity = activity ,
                barcodeDataStore = dataStore ,
                scannerView = view ,
                enableFlash = isFlashEnabled ,
                useBackCamera = isBackCamera ,
                useSimpleAutoFocus = simpleAutoFocus ,
                scanContinuously = continuousScanning ,
                requireConfirmation = confirmScansManually ,
                saveToHistory = saveScannedBarcodesToHistory ,
                skipDuplicates = doNotSaveDuplicates ,
                barcodeDecoder = barcodeParser ,
                barcodeRepository = barcodeDatabase ,
                navigateToDetailsScreen = navigateToBarcodeScreen ,
                handleExternalScanResult = handleScannedData ,
                externalScanIntentAction = zxingScanIntentAction
            )
        }
        else {
            NoCameraPermissionScreen(onRequestPermission = {
                AppPermissionsManager.requestCameraPermission(activity)
                hasCameraPermission = AppPermissionsManager.hasCameraPermission(context)
            })
        }
    }
}