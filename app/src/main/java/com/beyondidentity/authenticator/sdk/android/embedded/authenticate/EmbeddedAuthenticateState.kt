package com.beyondidentity.authenticator.sdk.android.embedded.authenticate

data class EmbeddedAuthenticateState(
    val getAuthenticationContextResult: String = "",
    val getAuthenticationContextProgress: Boolean = false,
    val authenticateBeyondIdentityResult: String = "",
    val authenticateBeyondIdentityProgress: Boolean = false,
    val authenticateOktaSDKResult: String = "",
    val authenticateOktaSDKProgress: Boolean = false,
    val authenticateOktaWebResult: String = "",
    val authenticateOktaWebProgress: Boolean = false,
    val authenticateAuth0SDKResult: String = "",
    val authenticateAuth0SDKProgress: Boolean = false,
    val authenticateAuth0WebResult: String = "",
    val authenticateAuth0WebProgress: Boolean = false,
    val authenticateUrl: String = "",
    val authenticateResult: String = "",
    val authenticateProgress: Boolean = false,
    val authenticateEmailOtpUrl: String = "",
    val authenticateEmailOtpResult: String = "",
    val authenticateEmailOtpProgress: Boolean = false,
    val redeemEmailOtpUrl: String = "",
    val redeemEmailOtpResult: String = "",
    val redeemEmailOtpProgress: Boolean = false,
    val buttonPressed: String = "",
    val codeChallenge: String = "",
    val codeVerifier: String = "",
    val emailOtpUrl: String = ""
)

sealed class EmbeddedAuthenticateEvents {
    data class AuthenticateEvent(val result: String) : EmbeddedAuthenticateEvents()
}
