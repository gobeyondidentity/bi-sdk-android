package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.AuthenticationContext as BiAuthenticationContext

/**
 * Information associated with the current authentication request returned from `getAuthenticationContext`.
 *
 * Note that the `authUrl` field may differ from the URL passed into `getAuthenticationContext`. In this event, the new `authUrl` must be passed into `authenticate` or `authenticateOtp`, rather than the original URL.
 */
data class AuthenticationContext(
    val authUrl: String,
    val application: Application,
    val origin: Origin,
) {
    data class Origin(
        val sourceIp: String?,
        val userAgent: String?,
        val geolocation: String?,
        val referer: String?
    )

    data class Application(
        val id: String,
        val displayName: String?,
    )

    companion object {
        fun from(authenticateResponse: BiAuthenticationContext) =
            AuthenticationContext(
                authUrl = authenticateResponse.authUrl,
                application = Application(
                    id = authenticateResponse.application.id,
                    displayName = authenticateResponse.application.displayName
                ),
                origin = Origin(
                    sourceIp = authenticateResponse.origin.sourceIp,
                    userAgent = authenticateResponse.origin.userAgent,
                    geolocation = authenticateResponse.origin.geolocation,
                    referer = authenticateResponse.origin.referer
                ),
            )
    }
}
