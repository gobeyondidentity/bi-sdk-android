package com.beyondidentity.authenticator.sdk.android.embedded

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.beyondidentity.authenticator.sdk.android.App.Companion.EMBEDDED_KEYGUARD_REQUEST
import com.beyondidentity.authenticator.sdk.android.R
import com.beyondidentity.authenticator.sdk.android.embedded.regandrecover.EmbeddedUIRegAndRecoverActivity
import com.beyondidentity.embedded.embeddedui.ui.ActionType.Registration
import com.beyondidentity.embedded.embeddedui.ui.BeyondIdentityActionHandlerFragment
import com.beyondidentity.embedded.embeddedui.ui.BeyondIdentitySettingsFragment
import com.beyondidentity.embedded.embeddedui.ui.continueWithBeyondIdentity
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.Authentication
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.Authorization
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.BiEventError
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.CredentialDeleted
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.CredentialRecovery
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.CredentialRegistered
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.CredentialSetup
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiObserver
import com.beyondidentity.embedded.embeddedui.ui.views.BeyondIdentityButton
import com.beyondidentity.embedded.sdk.EmbeddedSdk

class EmbeddedUISdkLoginActivity : AppCompatActivity(), BiObserver {
    private lateinit var signInButton: BeyondIdentityButton
    private lateinit var settingsButton: AppCompatTextView
    private lateinit var functionOnlyButton: AppCompatTextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_embedded_login)
        signInButton = findViewById(R.id.sign_in_button)
        settingsButton = findViewById(R.id.settings_button)
        functionOnlyButton = findViewById(R.id.function_only_button)

        intent.data?.let { registerUri ->
            val loadingBi = BeyondIdentityActionHandlerFragment
                .newInstance(actionType = Registration(registerUri = registerUri.toString()))

            loadingBi.show(supportFragmentManager, BeyondIdentityActionHandlerFragment.TAG)
        }

        signInButton.setViewData(supportFragmentManager)
        functionOnlyButton.setOnClickListener { continueWithBeyondIdentity(supportFragmentManager) }

        settingsButton.setOnClickListener {
            val settingsFragment = BeyondIdentitySettingsFragment
                .newInstance()

            settingsFragment.show(supportFragmentManager, BeyondIdentitySettingsFragment.TAG)
        }
        BiEventBus.registerObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        BiEventBus.unRegisterObserver(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EMBEDDED_KEYGUARD_REQUEST) {
            when (resultCode) {
                RESULT_OK -> EmbeddedSdk.answer(true)
                RESULT_CANCELED -> EmbeddedSdk.answer(false)
            }
        }
    }

    override fun onEvent(event: BiEvent) {
        when (event) {
            CredentialSetup -> {
                startActivity(Intent(this, EmbeddedUIRegAndRecoverActivity::class.java))
            }
            CredentialRecovery -> {
                startActivity(Intent(this, EmbeddedUIRegAndRecoverActivity::class.java))
            }
            is Authentication -> {
                Toast.makeText(
                    this,
                    "Token received ${event.tokenResponse}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is Authorization -> {
                Toast.makeText(
                    this,
                    "Authorization ${event.authorizationCode}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is BiEventError -> {
                Toast.makeText(
                    this,
                    "Error ${event.throwable}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is CredentialRegistered -> {
                Toast.makeText(
                    this,
                    "Credential registered ${event.credential}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            CredentialDeleted -> {
                Toast.makeText(this, "Credential deleted", Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }
}