package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.BiAuthenticateResponse
import org.junit.Test

class AuthenticateResponseTest {
    @Test
    fun checkFromBiAuthenticateResponseSuccess() {
        val MESSAGE =
            ""
        val REDIRECT_URL =
            "http://example.com/?code=0123456789ABCDEF&state=foobar"

        val biAuthenticateResponse = BiAuthenticateResponse(
            message = MESSAGE,
            redirectUrl = REDIRECT_URL,
        )

        val authenticateResponse = AuthenticateResponse.from(biAuthenticateResponse)

        assert(
            authenticateResponse.message.equals(
                biAuthenticateResponse.message,
                ignoreCase = false,
            )
        )
        assert(
            authenticateResponse.redirectUrl.equals(
                biAuthenticateResponse.redirectUrl,
                ignoreCase = false,
            )
        )
    }
}
