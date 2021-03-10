package com.beyondidentity.authenticator.sdk.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beyondidentity.authenticator.sdk.AuthenticatorSignInButton

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val logInButton = findViewById<AuthenticatorSignInButton>(R.id.my_login_button)

        logInButton.setPasswordlessLogInListener(
            loginUrl = "$ACME_CLOUD_URL/start",
            redirectUrl = "${getString(R.string.app_scheme)}://${getString(R.string.app_host)}",
        )
    }
}