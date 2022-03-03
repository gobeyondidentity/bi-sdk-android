package com.beyondidentity.authenticator.sdk.android.embedded.regandrecover

data class RegAndRecoverState(
    val registerEmail: String = "",
    val registerResult: String = "",
    val recoverEmail: String = "",
    val recoverResult: String = "",
)
