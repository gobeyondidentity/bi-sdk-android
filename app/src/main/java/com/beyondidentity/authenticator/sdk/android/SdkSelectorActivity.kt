package com.beyondidentity.authenticator.sdk.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beyondidentity.authenticator.sdk.android.authenticator.AuthenticatorSignInActivity
import com.beyondidentity.authenticator.sdk.android.embedded.EmbeddedAllFunctionalityActivity
import com.beyondidentity.authenticator.sdk.android.embedded.EmbeddedLoginActivity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SdkSelectorActivity : AppCompatActivity() {
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdk_selector)

        findViewById<MaterialButton>(R.id.embedded_sdk_select_button).setOnClickListener {
            startActivity(Intent(this, EmbeddedAllFunctionalityActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.embedded_sdk_select_button_ui).setOnClickListener {
            startActivity(Intent(this, EmbeddedLoginActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.authenticator_sdk_select_button).setOnClickListener {
            startActivity(Intent(this, AuthenticatorSignInActivity::class.java))
        }
    }
}