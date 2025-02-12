package com.d4rk.qrcodescanner.plus.utils.extensions

import org.apache.commons.codec.binary.Base32

fun ByteArray.encodeBase32() : String {
    return Base32().encodeAsString(this)
}