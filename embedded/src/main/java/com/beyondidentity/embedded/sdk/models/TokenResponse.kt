package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.EmbeddedPublicOidcResponse

/**
 * OAuth token grant
 *
 * @property accessToken  OAuth token grant
 * @property idToken OIDC JWT token grant
 * @property tokenType type such as "Bearer"
 * @property expiresIn [accessToken] expiration
 */
data class TokenResponse(
    val accessToken: String,
    val idToken: String,
    val tokenType: String,
    val expiresIn: Long,
) {
    companion object {
        fun from(oidcResponse: EmbeddedPublicOidcResponse) =
            TokenResponse(
                accessToken = oidcResponse.accessToken,
                idToken = oidcResponse.idToken,
                tokenType = oidcResponse.tokenType,
                expiresIn = oidcResponse.expiresIn,
            )
    }
}
