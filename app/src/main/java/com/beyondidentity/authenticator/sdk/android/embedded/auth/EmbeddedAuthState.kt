package com.beyondidentity.authenticator.sdk.android.embedded.auth

import com.beyondidentity.embedded.sdk.models.PkceResponse

data class EmbeddedAuthState(
    val authenticationResult: String = "",
    val pkce: PkceResponse? = null,
    val pkceResult: String = "",
    val authorizeCode: String = "",
    val authorizeResult: String = "",
    val authorizeExchangeResult: String = "",
)