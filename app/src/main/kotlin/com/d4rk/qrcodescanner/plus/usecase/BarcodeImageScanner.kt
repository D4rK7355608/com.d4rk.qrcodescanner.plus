package com.d4rk.qrcodescanner.plus.usecase
import android.graphics.Bitmap
import com.d4rk.qrcodescanner.plus.extension.orZero
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.schedulers.Schedulers
object BarcodeImageScanner {
    private var bitmapBuffer: IntArray? = null
    fun parse(image: Bitmap): Single<Result> {
        return Single
            .create { emitter ->
                parse(image, emitter)
            }
            .subscribeOn(Schedulers.newThread())
    }
    private fun parse(image: Bitmap, emitter: SingleEmitter<Result>) {
        try {
            emitter.onSuccess(tryParse(image))
        } catch (ex: Exception) {
            emitter.onError(ex)
        }
    }
    private fun tryParse(image: Bitmap): Result {
        val width = image.width
        val height = image.height
        val size = width * height
        if (size > bitmapBuffer?.size.orZero()) {
            bitmapBuffer = IntArray(size)
        }
        bitmapBuffer?.let { image.getPixels(it, 0, width, 0, 0, width, height) }
        val source = RGBLuminanceSource(width, height, bitmapBuffer)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        val reader = MultiFormatReader()
        return reader.decode(bitmap)
    }
}