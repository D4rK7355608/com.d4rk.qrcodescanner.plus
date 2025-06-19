package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

/**
 * Traverses the context chain and returns the first [ComponentActivity] if present.
 */
fun Context.findActivity(): ComponentActivity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is AppCompatActivity) return ctx
        if (ctx is ComponentActivity) return ctx
        ctx = ctx.baseContext
    }
    return null
}
