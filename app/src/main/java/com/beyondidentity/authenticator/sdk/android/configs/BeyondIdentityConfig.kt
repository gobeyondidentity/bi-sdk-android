package com.beyondidentity.authenticator.sdk.android.configs

import java.net.URLEncoder

object BeyondIdentityConfig {
    // Endpoints
    const val ISSUER_ENDPOINT =
        "https://auth-us.beyondidentity.com/v1/tenants/00012da391ea206d/realms/862e4b72cfdce072/applications/3d869893-08b1-46ca-99c7-3c12226edf1b"
    const val AUTHORIZATION_ENDPOINT =
        "https://auth-us.beyondidentity.com/v1/tenants/00012da391ea206d/realms/862e4b72cfdce072/applications/3d869893-08b1-46ca-99c7-3c12226edf1b/authorize"
    const val TOKEN_ENDPOINT =
        "https://auth-us.beyondidentity.com/v1/tenants/00012da391ea206d/realms/862e4b72cfdce072/applications/3d869893-08b1-46ca-99c7-3c12226edf1b/token"
    const val JWKS_ENDPOINT =
        "https://auth-us.beyondidentity.com/v1/tenants/00012da391ea206d/realms/862e4b72cfdce072/applications/3d869893-08b1-46ca-99c7-3c12226edf1b/.well-known/jwks.json"

    const val SCOPE = "openid"
    const val CLIENT_ID = "JvV5DbxFZbana_tMTAPTs-gY"
    const val REDIRECT_URI = "acme://"

    fun getAuthorizeUrl(
        scope: String = SCOPE,
        response_type: String = "code",
        client_id: String = CLIENT_ID,
        redirect_uri: String = REDIRECT_URI,
    ): String {
        return AUTHORIZATION_ENDPOINT + "?" +
                "scope=$scope" + "&" +
                "response_type=$response_type" + "&" +
                "redirect_uri=${URLEncoder.encode(redirect_uri, "utf-8")}" + "&" +
                "client_id=$client_id"
    }
}
