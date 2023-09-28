package com.beyondidentity.authenticator.sdk.android.embedded.utils

import androidx.lifecycle.ViewModel

@Suppress("UnusedReceiverParameter")
fun ViewModel.resetResult(
    progress: Boolean = true,
    updateStateCallback: UpdateStateCallback,
) {
    updateStateCallback.invoke("", "", progress)
}

@Suppress("UnusedReceiverParameter")
fun ViewModel.resetResult(
    string: String,
    result: String,
    progress: Boolean = true,
    updateStateCallback: UpdateStateCallback,
): Boolean {
    updateStateCallback.invoke("", "", progress)

    if (string.isEmpty()) {
        updateStateCallback.invoke("", result, false)
        return false
    }

    return true
}
