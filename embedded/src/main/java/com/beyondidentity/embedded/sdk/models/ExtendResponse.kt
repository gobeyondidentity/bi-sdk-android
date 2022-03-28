package com.beyondidentity.embedded.sdk.models

import android.graphics.Bitmap

/**
 *  A random 9 digit token associated with a list of Credentials being registered or extended
 *
 * @property rendezvousToken string value for the random 9 digit token
 * @property rendezvousTokenBitmap [Bitmap] of the random 9 digit code as QR code
 */
data class ExtendResponse(
    val rendezvousToken: String,
    val rendezvousTokenBitmap: Bitmap,
)
