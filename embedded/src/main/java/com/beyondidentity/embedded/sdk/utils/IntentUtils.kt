package com.beyondidentity.embedded.sdk.utils

import android.content.Intent
import android.net.Uri

fun supportIntent(url: String) = if (url.contains("@")) {
    Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$url")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
} else {
    Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
