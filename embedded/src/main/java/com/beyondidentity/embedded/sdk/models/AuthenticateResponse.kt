package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.BiAuthenticateResponse
import com.beyondidentity.sdk.android.bicore.models.BiAuthenticateUrlResponse
import com.beyondidentity.sdk.android.bicore.models.UrlDataResponse

/**
 * A response returned after successfully authenticating.
 *
 * @property redirectUrl The redirect URL that originates from the /authorize call's `redirect_uri` parameter. The OAuth2 authorization `code` and the `state` parameter of the /authorize call are attached with the "code" and "state" parameters to this URL.
 * @property message An optional displayable message defined by policy returned by the cloud on success
 * @property passkeyBindingToken An optional one-time-token returned from successful `redeemOtp` that may be redeemed for a credential_binding_link from the /credential-binding-jobs endpoint.
 */
data class AuthenticateResponse(
    val redirectUrl: String,
    val message: String? = null,
    var passkeyBindingToken: String? = null
) {
    companion object {
        fun from(authenticateResponse: BiAuthenticateUrlResponse) = AuthenticateResponse(
            redirectUrl = authenticateResponse.redirectUrl,
            message = authenticateResponse.message,
            passkeyBindingToken = authenticateResponse.passkeyBindingToken
        )

        fun from(urlDataResponse: UrlDataResponse) = urlDataResponse.biAuthenticate?.let { biAuthenticateResponse ->
            from(biAuthenticateResponse)
        }

        fun from(urlDataResponse: BiAuthenticateResponse) = urlDataResponse.allow?.let { biAuthenticateResponse ->
            from(biAuthenticateResponse)
        }
    }
}
