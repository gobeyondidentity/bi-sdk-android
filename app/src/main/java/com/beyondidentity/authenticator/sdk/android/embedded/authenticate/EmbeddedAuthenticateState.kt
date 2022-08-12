package com.beyondidentity.authenticator.sdk.android.embedded.authenticate

data class EmbeddedAuthenticateState(
    val authenticateBeyondIdentityResult: String = "",
    val authenticateOktaSDKResult: String = "",
    val authenticateOktaWebResult: String = "",
    val authenticateAuth0SDKResult: String = "",
    val authenticateAuth0WebResult: String = "",
    val authenticateCognitoResult: String = "",
    val authenticateUrl: String = "",
    val authenticateResult: String = "",
    val buttonPressed: String = "",
    val codeChallenge: String = "",
    val codeVerifier: String = "",
)

sealed class EmbeddedAuthenticateEvents {
    data class AuthenticateEvent(val result: String) : EmbeddedAuthenticateEvents()
}
