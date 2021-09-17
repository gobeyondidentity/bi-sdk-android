package com.beyondidentity.embedded.sdk.utils

import android.os.Looper
import androidx.core.os.HandlerCompat

val handler = HandlerCompat.createAsync(Looper.getMainLooper())

/**
 * Posts a runnable to the MainLooper message queue
 *
 * @param block code to be executed on main thread
 */
fun postMain(block: () -> Unit) {
    handler.post(block)
}
