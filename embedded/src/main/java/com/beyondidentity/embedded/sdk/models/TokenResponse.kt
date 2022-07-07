package com.beyondidentity.embedded.sdk.models

/**
 * OAuth token grant
 *
 * @property accessToken OAuth token grant
 * @property idToken OIDC JWT token grant
 * @property tokenType type such as "Bearer"
 * @property expiresIn [accessToken] expiration
 */
data class TokenResponse(
    val accessToken: String,
    val idToken: String,
    val tokenType: String,
    val expiresIn: Long,
)
