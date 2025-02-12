package com.d4rk.qrcodescanner.plus.utils.extensions

fun <T> unsafeLazy(initializer : () -> T) : Lazy<T> = lazy(LazyThreadSafetyMode.NONE , initializer)