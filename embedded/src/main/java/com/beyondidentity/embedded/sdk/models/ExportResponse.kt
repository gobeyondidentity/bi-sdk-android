package com.beyondidentity.embedded.sdk.models

import android.graphics.Bitmap

/**
 *  A random 9 digit token associated with a list of Credetials being imported or exported
 *
 * @property rendezvousToken string value for the random 9 digit token
 * @property rendezvousTokenBitmap [Bitmap] of the random 9 digit code as QR code
 */
data class ExportResponse(
    val rendezvousToken: String,
    val rendezvousTokenBitmap: Bitmap,
)
