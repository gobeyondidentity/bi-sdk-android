package com.beyondidentity.embedded.sdk.utils

import android.content.Intent
import android.net.Uri

fun supportEmailIntent(email: String) = Intent(Intent.ACTION_SENDTO).apply {
    data = Uri.parse("mailto:$email")
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}
