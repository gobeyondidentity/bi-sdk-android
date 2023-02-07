package com.beyondidentity.authenticator.sdk.android.embedded.urlvalidation

data class EmbeddedUrlValidationState(
    val urlValidationBindPasskeyUrl: String = "",
    val validateBindPasskeyUrlResult: String = "",
    val validateBindPasskeyUrlProgress: Boolean = false,
    val urlValidationAuthenticateUrl: String = "",
    val validateAuthenticateUrlResult: String = "",
    val validateAuthenticateUrlProgress: Boolean = false,
)
