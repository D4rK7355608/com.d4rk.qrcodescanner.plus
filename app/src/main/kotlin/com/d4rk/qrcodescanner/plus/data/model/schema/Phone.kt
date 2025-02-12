package com.d4rk.qrcodescanner.plus.data.model.schema

import com.d4rk.qrcodescanner.plus.utils.extensions.removePrefixIgnoreCase
import com.d4rk.qrcodescanner.plus.utils.extensions.startsWithIgnoreCase
import com.d4rk.qrcodescanner.plus.model.schema.BarcodeSchema
import com.d4rk.qrcodescanner.plus.model.schema.Schema

class Phone(val phone : String) : Schema {

    companion object {
        private const val PREFIX = "tel:"

        fun parse(text : String) : Phone? {
            if (text.startsWithIgnoreCase(PREFIX).not()) {
                return null
            }

            val phone = text.removePrefixIgnoreCase(PREFIX)
            return Phone(phone)
        }
    }

    override val schema = BarcodeSchema.PHONE
    override fun toFormattedText() : String = phone
    override fun toBarcodeText() : String = "$PREFIX$phone"
}