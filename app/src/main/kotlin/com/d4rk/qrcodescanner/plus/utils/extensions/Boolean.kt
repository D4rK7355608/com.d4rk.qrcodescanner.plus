package com.d4rk.qrcodescanner.plus.utils.extensions

fun Boolean?.orFalse() : Boolean {
    return this ?: false
}