package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.BiAuthenticateResponse
import com.beyondidentity.sdk.android.bicore.models.UrlDataResponse

/**
 * A response returned after successfully authenticating.
 *
 * @property operation "URLBiAuthenticateResponse" for this message
 * @property redirectUrl The redirect URL that originates from the /authorize call's `redirect_uri` parameter. The OAuth 2 authorization `code` and the `state` parameter of the /authorize call are attached with the "code" and "state" parameters to this URL.
 * @property message An optional displayable message defined by policy returned by the cloud on success
 */
data class AuthenticateResponse(
    val operation: String? = null,
    val redirectUrl: String? = null,
    val message: String? = null,
) {
    companion object {
        fun from(biAuthenticateResponse: BiAuthenticateResponse) =
            AuthenticateResponse(
                operation = biAuthenticateResponse.operation,
                redirectUrl = biAuthenticateResponse.redirectUrl,
                message = biAuthenticateResponse.message,
            )

        fun from(urlDataResponse: UrlDataResponse) =
            AuthenticateResponse(
                operation = urlDataResponse.biAuthenticate?.operation,
                redirectUrl = urlDataResponse.biAuthenticate?.redirectUrl,
                message = urlDataResponse.biAuthenticate?.message,
            )
    }
}
