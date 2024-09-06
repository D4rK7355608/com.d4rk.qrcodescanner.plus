package com.d4rk.qrcodescanner.plus.data.model.schema

import com.d4rk.qrcodescanner.plus.model.schema.BarcodeSchema
import com.d4rk.qrcodescanner.plus.model.schema.Schema

class Other(val text : String) : Schema {
    override val schema = BarcodeSchema.OTHER
    override fun toFormattedText() : String = text
    override fun toBarcodeText() : String = text
}