package com.beyondidentity.embedded.sdk.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

object Qr {
    const val QR_WIDTH = 1000
    const val QR_HEIGHT = 1000

    fun generateQrCode(text: String): Bitmap =
        BarcodeEncoder().encodeBitmap(text, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT)
}
