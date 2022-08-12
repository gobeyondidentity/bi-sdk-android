package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.UrlDataResponse
import com.beyondidentity.sdk.android.bicore.models.BiAuthenticateResponse as BiAuthenticateResponse

/**
 * A response returned after successfully authenticating.
 *
 * @property redirectUrl The redirect URL that originates from the /authorize call's `redirect_uri` parameter. The OAuth2 authorization `code` and the `state` parameter of the /authorize call are attached with the "code" and "state" parameters to this URL.
 * @property message An optional displayable message defined by policy returned by the cloud on success
 */
data class AuthenticateResponse(
    val redirectUrl: String? = null,
    val message: String? = null,
) {
    companion object {
        fun from(authenticateResponse: BiAuthenticateResponse) =
            AuthenticateResponse(
                redirectUrl = authenticateResponse.redirectUrl,
                message = authenticateResponse.message,
            )

        fun from(urlDataResponse: UrlDataResponse) =
            urlDataResponse.biAuthenticate?.let { biAuthenticateResponse ->
                from(biAuthenticateResponse)
            }
    }
}
