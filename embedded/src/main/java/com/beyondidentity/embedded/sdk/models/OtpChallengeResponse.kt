package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.BiAuthenticateResponse
import com.beyondidentity.sdk.android.bicore.models.BiContinueResponse
import com.beyondidentity.sdk.android.bicore.models.UrlDataResponse

/**
 * A response returned if the SDK requires an OTP.
 *
 * @property url A URL containing the state of the current authentication transaction. This should be used in the next `redeemOtp` or `authenticateOtp` function.
 */
data class OtpChallengeResponse(val url: String) {
    companion object {
        fun from(biContinueResponse: BiContinueResponse) = OtpChallengeResponse(
            url = biContinueResponse.url
        )

        fun from(urlDataResponse: UrlDataResponse) = urlDataResponse.biContinue?.let { biContinueResponse ->
            from(biContinueResponse)
        }

        fun from(urlDataResponse: BiAuthenticateResponse) = urlDataResponse.`continue`?.let { biContinueResponse ->
            from(biContinueResponse)
        }
    }
}
