package com.beyondidentity.authenticator.sdk.android.configs

import com.beyondidentity.authenticator.sdk.android.utils.PKCEUtil
import java.net.URLEncoder

object BeyondIdentityConfig {
    // Endpoints
    const val ISSUER_ENDPOINT =
        "https://auth-us.beyondidentity.com/v1/tenants/00012da391ea206d/realms/862e4b72cfdce072/applications/a8c0aa60-38e4-42b6-bd52-ef64aba5478b"
    const val AUTHORIZATION_ENDPOINT =
        "https://auth-us.beyondidentity.com/v1/tenants/00012da391ea206d/realms/862e4b72cfdce072/applications/a8c0aa60-38e4-42b6-bd52-ef64aba5478b/authorize"
    const val TOKEN_ENDPOINT =
        "https://auth-us.beyondidentity.com/v1/tenants/00012da391ea206d/realms/862e4b72cfdce072/applications/a8c0aa60-38e4-42b6-bd52-ef64aba5478b/token"
    const val JWKS_ENDPOINT =
        "https://auth-us.beyondidentity.com/v1/tenants/00012da391ea206d/realms/862e4b72cfdce072/applications/a8c0aa60-38e4-42b6-bd52-ef64aba5478b/.well-known/jwks.json"

    const val SCOPE = "openid"
    const val CLIENT_ID = "KhSWSmfhZ6xCMz9yw7DpJcv5"
    const val REDIRECT_URI = "acme://"

    fun getAuthorizeUrl(
        scope: String = SCOPE,
        response_type: String = "code",
        client_id: String = CLIENT_ID,
        code_challenge_method: String = "S256",
        redirect_uri: String = REDIRECT_URI,
        code_challenge: String = PKCEUtil.generateCodeChallenge(PKCEUtil.generateCodeVerifier()),
    ): String {
        return AUTHORIZATION_ENDPOINT + "?" +
                "scope=$scope" + "&" +
                "response_type=$response_type" + "&" +
                "code_challenge_method=$code_challenge_method" + "&" +
                "redirect_uri=${URLEncoder.encode(redirect_uri, "utf-8")}" + "&" +
                "code_challenge=$code_challenge" + "&" +
                "client_id=$client_id"
    }
}
