package com.beyondidentity.embedded.sdk.utils

import android.content.Intent
import androidx.core.net.toUri

fun supportIntent(url: String) = if (url.contains("@")) {
    Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:$url".toUri()
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
} else {
    Intent(Intent.ACTION_VIEW).apply {
        data = url.toUri()
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
