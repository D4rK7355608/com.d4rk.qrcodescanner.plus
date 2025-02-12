package com.d4rk.qrcodescanner.plus.utils.extensions

fun Double?.orZero() : Double {
    return this ?: 0.0
}