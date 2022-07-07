package com.beyondidentity.embedded.sdk.models

/**
 * Represent PKCE authorization request parameters.
 *
 * @property codeVerifier A cryptographically random string.
 * @property codeChallenge A challenge derived from the code verifier.
 * @property codeChallengeMethod A method that was used to derive code challenge.
 */
data class PkceResponse(
    val codeVerifier: String,
    val codeChallenge: String,
    val codeChallengeMethod: String,
)
