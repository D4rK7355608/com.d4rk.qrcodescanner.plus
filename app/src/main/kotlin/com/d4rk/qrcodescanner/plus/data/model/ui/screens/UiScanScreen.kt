package com.d4rk.qrcodescanner.plus.data.model.ui.screens

import com.d4rk.qrcodescanner.plus.data.model.Barcode

data class UiScanScreen(
    val showConfirmationDialog: Boolean = false ,
    val scannedBarcode: Barcode? = null ,
    val lastResult: Barcode? = null ,
    val maxZoom: Int = 0 ,
    val zoom: Int = 0 ,
)