package com.beyondidentity.authenticator.sdk.android.configs

import android.net.Uri
import com.beyondidentity.authenticator.sdk.android.utils.PKCEUtil
import java.net.URLEncoder

object Auth0Config {
    // Auth0
    const val CLIENT_ID = "q1cubQfeZWnajq5YkeZVD3NauRqU4vNs"
    const val DOMAIN = "dev-pt10fbkg.us.auth0.com"

    const val CONNECTION = "Example-App-Native"
    const val SCOPE = "openid"
    const val REDIRECT_URI = "https://dev-pt10fbkg.us.auth0.com/login/callback"

    const val WEB_REDIRECT_URI = "acme://auth0"

    fun isRedirectUri(uri: Uri): Boolean = uri.toString().startsWith(REDIRECT_URI) ||
        uri.toString().startsWith(WEB_REDIRECT_URI)

    fun getPkceAuthorizeUrl(
        connection: String = CONNECTION,
        scope: String = SCOPE,
        response_type: String = "code",
        state: String = "state",
        code_challenge_method: String = "S256",
        redirect_uri: String = REDIRECT_URI,
        nonce: String = "nonce",
        code_challenge: String = PKCEUtil.generateCodeChallenge(PKCEUtil.generateCodeVerifier()),
        client_id: String = CLIENT_ID
    ): String = "https://dev-pt10fbkg.us.auth0.com/authorize?" +
        "connection=$connection" + "&" +
        "scope=$scope" + "&" +
        "response_type=$response_type" + "&" +
        "state=$state" + "&" +
        "code_challenge_method=$code_challenge_method" + "&" +
        "redirect_uri=${URLEncoder.encode(redirect_uri, "utf-8")}" + "&" +
        "nonce=$nonce" + "&" +
        "code_challenge=$code_challenge" + "&" +
        "client_id=$client_id"
}
