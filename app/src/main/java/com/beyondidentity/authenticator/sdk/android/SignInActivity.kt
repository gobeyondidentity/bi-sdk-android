package com.beyondidentity.authenticator.sdk.android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beyondidentity.authenticator.sdk.AuthView

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val logInButton = findViewById<AuthView>(R.id.auth_view)

        logInButton.initAuthView(
            loginUrl = "$ACME_CLOUD_URL/start",
            redirectUrl = "${getString(R.string.app_scheme)}://${getString(R.string.app_host)}",
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
