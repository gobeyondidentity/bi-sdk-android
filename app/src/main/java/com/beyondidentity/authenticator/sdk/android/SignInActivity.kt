package com.beyondidentity.authenticator.sdk.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.beyondidentity.authenticator.sdk.AuthView

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val logInButton = findViewById<AuthView>(R.id.my_login_button)

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