package com.d4rk.qrcodescanner.plus.utils.extensions

fun Int?.orZero() : Int {
    return this ?: 0
}