package com.beyondidentity.authenticator.sdk.android.embedded

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.auth0.android.jwt.JWT
import com.beyondidentity.authenticator.sdk.android.BuildConfig
import com.beyondidentity.authenticator.sdk.android.R
import com.beyondidentity.authenticator.sdk.android.utils.CreateUserRequest
import com.beyondidentity.authenticator.sdk.android.utils.RecoverUserRequest
import com.beyondidentity.authenticator.sdk.android.utils.RetrofitBuilder
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.util.UUID
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

const val EMBEDDED_KEYGUARD_REQUEST = 2314

@ExperimentalCoroutinesApi
class EmbeddedAllFunctionalityActivity : AppCompatActivity() {
    private var authzCode: String? = null
    private lateinit var credentialsButton: MaterialButton
    private lateinit var credentialsText: MaterialTextView
    private lateinit var deleteCredentialsButton: MaterialButton
    private lateinit var pkceButton: MaterialButton
    private lateinit var pkceText: MaterialTextView
    private lateinit var createUserButton: MaterialButton
    private lateinit var createUserText: MaterialTextView
    private lateinit var createUserInput: TextInputEditText
    private lateinit var recoverUserButton: MaterialButton
    private lateinit var recoverUserText: MaterialTextView
    private lateinit var recoverUserInput: TextInputEditText
    private lateinit var authConfidentialButton: MaterialButton
    private lateinit var authConfidentialText: MaterialTextView
    private lateinit var authConfidentialTokenButton: MaterialButton
    private lateinit var authConfidentialTokenText: MaterialTextView
    private lateinit var authPublicButton: MaterialButton
    private lateinit var authPublicText: MaterialTextView
    private lateinit var exportProfileButton: MaterialButton
    private lateinit var cancelExportButton: MaterialButton
    private lateinit var exportProfileText: MaterialTextView
    private lateinit var exportProfileQr: ImageView
    private lateinit var importProfileButton: MaterialButton
    private lateinit var importProfileText: TextInputEditText

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e("Caught $exception")
        Toast.makeText(this, "$exception", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_embedded_all_functionality)

        setupViews()

        intent.data?.let { uri ->
            Toast.makeText(this, "Registering Credential...", Toast.LENGTH_LONG).show()
            EmbeddedSdk.registerCredential(
                url = uri.toString(),
            ).onEach {
                it.onSuccess { p ->
                    Timber.d(p.toString())
                    Toast.makeText(this, "Credential created", Toast.LENGTH_SHORT).show()
                }
                it.onFailure { t ->
                    Timber.e(t)
                    Toast.makeText(this, "Failed to create credential", Toast.LENGTH_SHORT).show()
                }
            }
                .catch { credentialsText.text = it.message }
                .launchIn(lifecycleScope)
        }
    }

    private fun setupViews() {
        credentialsButton = findViewById(R.id.embedded_credentials_button)
        deleteCredentialsButton = findViewById(R.id.embedded_delete_credentials_button)
        credentialsText = findViewById(R.id.embedded_credentials_text)
        pkceButton = findViewById(R.id.embedded_signin_pkce_button)
        pkceText = findViewById(R.id.embedded_signin_pkce_text)
        createUserButton = findViewById(R.id.embedded_signin_createuser_button)
        createUserText = findViewById(R.id.embedded_signin_createuser_text)
        createUserInput = findViewById(R.id.embedded_signin_createuser_input)
        recoverUserButton = findViewById(R.id.embedded_signin_recoveruser_button)
        recoverUserText = findViewById(R.id.embedded_signin_recoveruser_text)
        recoverUserInput = findViewById(R.id.embedded_signin_recoveruser_input)
        authConfidentialButton = findViewById(R.id.embedded_signin_authconf_button)
        authConfidentialText = findViewById(R.id.embedded_signin_authconf_text)
        authConfidentialTokenButton = findViewById(R.id.embedded_signin_authconf_token_button)
        authConfidentialTokenText = findViewById(R.id.embedded_signin_authconf_token_text)
        authPublicButton = findViewById(R.id.embedded_signin_authpublic_button)
        authPublicText = findViewById(R.id.embedded_signin_authpublic_text)
        exportProfileButton = findViewById(R.id.embedded_exportprofile_button)
        cancelExportButton = findViewById(R.id.embedded_exportprofile_cancel_button)
        exportProfileText = findViewById(R.id.embedded_exportprofile_text)
        exportProfileQr = findViewById(R.id.embedded_exportprofile_qrcode)
        importProfileButton = findViewById(R.id.embedded_importprofile_button)
        importProfileText = findViewById(R.id.embedded_importprofile_text)

        credentialsButton.setOnClickListener {
            EmbeddedSdk.getCredentials()
                .onEach { result ->
                    result.onSuccess { creds -> credentialsText.text = creds.toString() }
                    result.onFailure { t -> Timber.e(t) }
                }
                .catch { credentialsText.text = it.message }
                .launchIn(lifecycleScope)
        }

        deleteCredentialsButton.setOnClickListener {
            EmbeddedSdk.deleteCredential(BuildConfig.BUILD_CONFIG_BEYOND_IDENTITY_DEMO_TENANT)
                .onEach { result ->
                    result.onSuccess {
                        Timber.d("Credentials deleted")
                        Toast.makeText(this, "Credentials deleted", Toast.LENGTH_SHORT).show()
                    }
                    result.onFailure {
                        Timber.d("Credentials deletion failed.")
                        Toast.makeText(this, "Credentials deletion failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .catch { Timber.d("Credentials deletion failed ${it.message}") }
                .launchIn(lifecycleScope)
        }

        pkceButton.setOnClickListener {
            EmbeddedSdk.createPkce()
                .onCompletion { Timber.d("PKCE Completed") }
                .onEach { result ->
                    result.onSuccess { pkce ->
                        Timber.d("got result for pkce = $pkce")
                        pkceText.text = pkce.toString()
                    }
                    result.onFailure { t -> Timber.e("error getting PKCE $t") }
                }
                .catch { pkceText.text = it.message }
                .launchIn(lifecycleScope)
        }

        createUserButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
                val result = RetrofitBuilder.ACME_API_SERVICE.createUser(
                    CreateUserRequest(
                        bindingTokenDeliveryMethod = "email",
                        externalId = createUserInput.text.toString(),
                        email = createUserInput.text.toString(),
                        displayName = UUID.randomUUID().toString(),
                        userName = UUID.randomUUID().toString(),
                    )
                )

                Timber.d("got result for createUser = $result")
                createUserText.text = result.toString()
            }
        }

        recoverUserButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
                val u = RetrofitBuilder.ACME_API_SERVICE.recoverUser(
                    RecoverUserRequest(
                        bindingTokenDeliveryMethod = "email",
                        externalId = recoverUserInput.text.toString(),
                    )
                )
                recoverUserText.text = u.toString()
            }
        }

        authConfidentialButton.setOnClickListener {
            EmbeddedSdk.authorize(
                clientId = BuildConfig.BUILD_CONFIG_BI_DEMO_CONFIDENTIAL_CLIENT_ID,
                redirectUri = "${BuildConfig.BUILD_CONFIG_BEYOND_IDENTITY_SDK_SAMPLEAPP_SCHEME}://",
                scope = "openid",
                pkceS256CodeChallenge = null,
            )
                .onEach { result ->
                    result.onSuccess { code ->
                        Timber.d("got result for auth confidential client = $code")
                        authzCode = code
                        authConfidentialText.text = "Authorization code = $code"
                    }
                    result.onFailure { t -> Timber.e("error confidential auth $t") }
                }
                .flowOn(Dispatchers.Main + coroutineExceptionHandler)
                .catch { authConfidentialText.text = it.message }
                .launchIn(lifecycleScope)
        }

        // !!! WARNING !!!
        // Never expose the client secret in your public clients (mobile app, front ent)
        // This is just for demo purposes.
        // Pass the authorization code to your backend, who can safely store the client secret,
        // to exchange the code for access and ID token.
        authConfidentialTokenButton.setOnClickListener {
            authzCode?.let { code ->
                lifecycleScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
                    val token = RetrofitBuilder.BI_API_SERVICE.getToken(
                        code = code,
                        redirectUri = "${BuildConfig.BUILD_CONFIG_BEYOND_IDENTITY_SDK_SAMPLEAPP_SCHEME}://",
                        grantType = "authorization_code",
                        code_verifier = null,
                    )

                    authConfidentialTokenText.text = parseIdToken(token.idToken)
                    Timber.d(token.toString())
                }
            } ?: run {
                Toast.makeText(this, "Get Authorization code first", Toast.LENGTH_SHORT).show()
            }
        }

        authPublicButton.setOnClickListener {
            EmbeddedSdk.authenticate(
                clientId = BuildConfig.BUILD_CONFIG_BI_DEMO_PUBLIC_CLIENT_ID,
                redirectUri = "${BuildConfig.BUILD_CONFIG_BEYOND_IDENTITY_SDK_SAMPLEAPP_SCHEME}://",
            )
                .onEach { result ->
                    result.onSuccess { response ->
                        lifecycleScope.launch(Dispatchers.Main) {
                            Timber.d("got result for auth confidential client = $response")
                            authPublicText.text = parseIdToken(response.idToken)
                        }
                    }
                    result.onFailure { t -> Timber.e("error confidential auth $t") }
                }
                .catch { authPublicText.text = it.message }
                .launchIn(lifecycleScope)
        }

        exportProfileText.copyOnHold()
        exportProfileButton.setOnClickListener {
            EmbeddedSdk.exportCredentials(
                credentialHandles = listOf(BuildConfig.BUILD_CONFIG_BEYOND_IDENTITY_DEMO_TENANT),
            )
                .onCompletion {
                    Timber.d("Export completed")
                    exportProfileText.text = "Export completed"
                    exportProfileQr.setImageDrawable(null)
                }
                .onEach {
                    Timber.d("Got export code = $it")
                    exportProfileText.text = it?.rendezvousToken
                    it?.let { exportProfileQr.setImageBitmap(it.rendezvousTokenBitmap) }
                }
                .catch {
                    Timber.e(it)
                    exportProfileText.text = it.message
                }
                .flowOn(Dispatchers.Main)
                .launchIn(lifecycleScope)
        }

        cancelExportButton.setOnClickListener {
            EmbeddedSdk.cancelExport()
                .onCompletion { Timber.d("Cancel completed") }
                .onEach { Timber.d(it.toString()) }
                .catch { Timber.e(it) }
                .launchIn(lifecycleScope)
        }

        importProfileButton.setOnClickListener {
            EmbeddedSdk.importCredentials(importProfileText.text.toString())
                .onCompletion { Timber.d("Import completed") }
                .onEach {
                    Timber.d("Imported profiles $it")
                    importProfileText.text?.clear()
                    Toast.makeText(this, "Profile imported", Toast.LENGTH_SHORT).show()
                }
                .catch { Timber.e(it) }
                .launchIn(lifecycleScope)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        EmbeddedSdk.cancelExport { }
    }

    private fun parseIdToken(idToken: String): String {
        val jwt = JWT(idToken)
        return jwt.claims.toList().fold("") { acc, pair ->
            acc + "${
                if (pair.first == "aud")
                    "${pair.first} = ${pair.second.asArray(String::class.java).joinToString()}"
                else
                    "${pair.first} = ${pair.second.asString()}"
            }\n"
        }
    }
}

fun MaterialTextView.copyOnHold(customText: String? = null) {
    setOnLongClickListener {
        val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied String", customText ?: text)
        clipboardManager.setPrimaryClip(clip)
        Toast.makeText(context, "Copied $text", Toast.LENGTH_SHORT).show()
        true // Or false if not consumed
    }
}