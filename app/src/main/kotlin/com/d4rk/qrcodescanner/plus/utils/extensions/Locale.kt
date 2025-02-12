package com.d4rk.qrcodescanner.plus.utils.extensions

import java.util.Locale

val Locale?.isRussian : Boolean get() = this?.language == "ru"