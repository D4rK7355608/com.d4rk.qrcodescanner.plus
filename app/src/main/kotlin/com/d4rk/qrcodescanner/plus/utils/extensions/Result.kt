package com.d4rk.qrcodescanner.plus.utils.extensions

import com.d4rk.qrcodescanner.plus.data.model.Barcode
import com.google.zxing.Result

fun Result.equalTo(barcode : Barcode?) : Boolean {
    return barcodeFormat == barcode?.format && text == barcode?.text
}