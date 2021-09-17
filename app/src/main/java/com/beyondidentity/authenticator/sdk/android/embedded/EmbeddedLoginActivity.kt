package com.beyondidentity.authenticator.sdk.android.embedded

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.beyondidentity.authenticator.sdk.android.R
import com.beyondidentity.embedded.embeddedui.ui.ActionType.Registration
import com.beyondidentity.embedded.embeddedui.ui.BeyondIdentityActionHandlerFragment
import com.beyondidentity.embedded.embeddedui.ui.BeyondIdentityButton
import com.beyondidentity.embedded.embeddedui.ui.BeyondIdentitySettingsFragment
import com.beyondidentity.embedded.embeddedui.ui.OnAuthenticationListener
import com.beyondidentity.embedded.embeddedui.ui.OnRegisterListener
import com.beyondidentity.embedded.sdk.models.Credential
import com.beyondidentity.embedded.sdk.models.TokenResponse
import timber.log.Timber

class EmbeddedLoginActivity : AppCompatActivity(), OnRegisterListener, OnAuthenticationListener {
    private lateinit var signInButton: BeyondIdentityButton
    private lateinit var settingsButton: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_embedded_login)
        signInButton = findViewById(R.id.sign_in_button)
        settingsButton = findViewById(R.id.settings_button)

        intent.data?.let { registerUri ->
            val loadingBi = BeyondIdentityActionHandlerFragment
                .newInstance(actionType = Registration(registerUri = registerUri.toString()))

            loadingBi.onRegisterListener = this
            loadingBi.onAuthenticationListener = this

            loadingBi.show(supportFragmentManager, BeyondIdentityActionHandlerFragment.TAG)
        }

        signInButton.setViewData(supportFragmentManager, this)

        settingsButton.setOnClickListener {
            BeyondIdentitySettingsFragment
                .newInstance()
                .show(supportFragmentManager, BeyondIdentitySettingsFragment.TAG)
        }
    }

    override fun onCredentialRegistered(credential: Credential) {
        Timber.d("credential registered = $credential")
    }

    override fun onCredentialRegistrationError(throwable: Throwable) {
        Timber.e("credential registration error $throwable")
    }

    override fun onPublicClientAuthentication(token: TokenResponse) {
        Timber.d("public client auth = $token")
    }

    override fun onConfidentialClientAuthentication(authorizationCode: String) {
        Timber.d("confidential client authz = $authorizationCode")
    }

    override fun onAuthenticationError(throwable: Throwable) {
        Timber.e("authentication failed $throwable")
    }
}