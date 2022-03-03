package com.beyondidentity.authenticator.sdk.android.embedded.extend

data class ExtendCredentialState(
    val generatedExtendToken: String = "",
    val cancelExtendResult: String = "",
    val registerTokenInputValue: String = "",
    val registerCredentialWithTokenResult: String = "",
)