package com.beyondidentity.authenticator.sdk.android

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beyondidentity.authenticator.sdk.AuthView
import com.beyondidentity.authenticator.sdk.android.utils.ACME_CLOUD_URL

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val logInButton = findViewById<AuthView>(R.id.auth_view)

        // example setup where the confidential client (backend) handles everything
        val url = "$ACME_CLOUD_URL/start"
        val redirectUrl = "${getString(R.string.app_scheme)}://${getString(R.string.app_host)}"

        val loginUriWithRedirect = Uri.parse(url).buildUpon().apply {
            appendQueryParameter("redirect", redirectUrl)
        }.build()

        // example setup for getting the authorization code on the public client
        // val uri = Uri.parse(authorizationEndpoint).buildUpon().apply {
        //     appendQueryParameter("client_id", clientId)
        //     appendQueryParameter("redirect_uri", redirectUri)
        //     appendQueryParameter("response_type", responseType)
        //     appendQueryParameter("scope", scope)
        //     appendQueryParameter("state", state)
        // }.build()

        logInButton.initAuthView(
            loginUri = loginUriWithRedirect,
            signupButtonListener = {
                Toast.makeText(
                    this,
                    "Sign up for Beyond Identity",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}
