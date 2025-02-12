package com.d4rk.qrcodescanner.plus.utils.extensions

fun Long?.orZero() : Long {
    return this ?: 0L
}