package com.d4rk.qrcodescanner.plus.extension
import android.os.VibrationEffect
import android.os.Vibrator
fun Vibrator.vibrateOnce(pattern: LongArray) {
    vibrate(VibrationEffect.createWaveform(pattern, -1))
}