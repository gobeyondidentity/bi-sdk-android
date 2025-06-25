package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.BiAuthenticateUrlResponse
import org.junit.Test

class AuthenticateResponseTest {

    companion object {
        private const val MESSAGE =
            ""

        private const val REDIRECT_URL =
            "http://example.com/?code=0123456789ABCDEF&state=foobar"
    }

    @Test
    fun checkFromBiAuthenticateResponseSuccess() {
        val biAuthenticateResponse = BiAuthenticateUrlResponse(
            message = MESSAGE,
            redirectUrl = REDIRECT_URL
        )

        val authenticateResponse = AuthenticateResponse.from(biAuthenticateResponse)

        assert(
            authenticateResponse.message.equals(
                biAuthenticateResponse.message,
                ignoreCase = false
            )
        )
        assert(
            authenticateResponse.redirectUrl.equals(
                biAuthenticateResponse.redirectUrl,
                ignoreCase = false
            )
        )
    }
}
