package com.beyondidentity.authenticator.sdk.android.configs

import android.net.Uri
import com.beyondidentity.authenticator.sdk.android.utils.PKCEUtil
import java.net.URLEncoder

object OktaConfig {
    const val DOMAIN = "dev-43409302.okta.com"

    // CustomConfiguration
    const val AUTHORIZATION_ENDPOINT = BeyondIdentityConfig.AUTHORIZATION_ENDPOINT
    const val TOKEN_ENDPOINT = BeyondIdentityConfig.TOKEN_ENDPOINT

    // OIDCConfig
    const val CLIENT_ID = "0oa5kipb8rdo4WCkf5d7"
    const val REDIRECT_URI = "com.okta.dev-43409302:/callback"
    const val END_SESSION_REDIRECT_URI = "com.okta.dev-43409302:/"
    const val SCOPES = "openid"
    const val DISCOVERY_URI = "https://$DOMAIN"

    const val IDP_ID = "0oa5rswruxTaPUcgl5d7"

    const val WEB_REDIRECT_URI = "acme://okta"

    fun isRedirectUri(uri: Uri): Boolean {
        return uri.toString().startsWith(REDIRECT_URI) ||
                uri.toString().startsWith(WEB_REDIRECT_URI)
    }

    fun getPkceAuthorizeUrl(
        idp: String = IDP_ID,
        scope: String = SCOPES,
        response_type: String = "code",
        state: String = "state",
        code_challenge_method: String = "S256",
        redirect_uri: String = REDIRECT_URI,
        nonce: String = "nonce",
        code_challenge: String = PKCEUtil.generateCodeChallenge(PKCEUtil.generateCodeVerifier()),
        client_id: String = CLIENT_ID,
    ): String {
        return "https://dev-43409302.okta.com/oauth2/v1/authorize?" +
                "idp=$idp" + "&" +
                "scope=$scope" + "&" +
                "response_type=$response_type" + "&" +
                "state=$state" + "&" +
                "code_challenge_method=$code_challenge_method" + "&" +
                "redirect_uri=${URLEncoder.encode(redirect_uri, "utf-8")}" + "&" +
                "nonce=$nonce" + "&" +
                "code_challenge=$code_challenge" + "&" +
                "client_id=$client_id"
    }
}
