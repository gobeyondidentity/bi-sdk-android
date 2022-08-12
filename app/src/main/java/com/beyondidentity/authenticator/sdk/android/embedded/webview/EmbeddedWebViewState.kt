package com.beyondidentity.authenticator.sdk.android.embedded.webview

data class EmbeddedWebViewState(
    val url: String = "",
    val result: String = "",
)

sealed class EmbeddedWebViewEvents {
    data class WebViewSuccess(
        val url: String,
        val result: String,
    ) : EmbeddedWebViewEvents()
}
