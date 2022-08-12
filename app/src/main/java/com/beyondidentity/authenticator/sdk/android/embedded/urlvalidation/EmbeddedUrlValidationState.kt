package com.beyondidentity.authenticator.sdk.android.embedded.urlvalidation

data class EmbeddedUrlValidationState(
    val urlValidationBindCredentialUrl: String = "",
    val validateBindCredentialUrlResult: String = "",
    val urlValidationAuthenticateUrl: String = "",
    val validateAuthenticateUrlResult: String = "",
)
