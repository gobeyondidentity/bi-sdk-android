@file:Suppress("unused")

package com.beyondidentity.embedded.sdk

import android.app.Application
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal
import com.beyondidentity.authenticator.sdk.embedded.BuildConfig
import com.beyondidentity.authenticator.sdk.embedded.R
import com.beyondidentity.embedded.sdk.export.ExportCredentialListener
import com.beyondidentity.embedded.sdk.models.Credential
import com.beyondidentity.embedded.sdk.models.ExportResponse
import com.beyondidentity.embedded.sdk.models.PkceResponse
import com.beyondidentity.embedded.sdk.models.TokenResponse
import com.beyondidentity.embedded.sdk.models.UserResponse
import com.beyondidentity.embedded.sdk.utils.Qr.generateQrCode
import com.beyondidentity.embedded.sdk.utils.appVersionName
import com.beyondidentity.embedded.sdk.utils.postMain
import com.beyondidentity.endpoint.android.lib.log.BiLogger
import com.beyondidentity.endpoint.android.lib.log.LogCategory
import com.beyondidentity.endpoint.android.lib.log.LogType
import com.beyondidentity.sdk.android.bicore.BiSdk
import com.beyondidentity.sdk.android.bicore.models.CodeChallenge
import com.beyondidentity.sdk.android.bicore.models.CoreExportStatus
import com.beyondidentity.sdk.android.bicore.models.PkceCodeChallengeMethod.S256
import com.beyondidentity.sdk.android.bicore.models.TrustedSource
import com.beyondidentity.sdk.android.bicore.models.UserRecoverRequest
import com.beyondidentity.sdk.android.bicore.partials.CoreFailure
import com.beyondidentity.sdk.android.bicore.partials.CoreSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.concurrent.Executors
import kotlin.Result.Companion

object EmbeddedSdk {
    const val BI_SUPPORT_EMAIL = "support@beyondidentity.com"

    data class Config(
        val appDisplayName: String,
        val supportEmail: String = BI_SUPPORT_EMAIL,
        val authenticationData: AuthenticationData,
    )

    sealed class AuthenticationData {

        data class PublicClientData(
            val clientId: String,
            val redirectUri: String,
        ) : AuthenticationData()

        data class ConfidentialClientData(
            val clientId: String,
            val redirectUri: String,
            val scope: String,
        ) : AuthenticationData()
    }

    private val answers = Channel<Boolean>()
    private lateinit var app: Application
    private lateinit var apiToken: String
    private var logger: ((String) -> Unit)? = null
    private var keyguardPrompt: (((allow: Boolean, exception: Exception?) -> Unit) -> Unit)? = null
    lateinit var config: Config

    private val executor = Executors.newFixedThreadPool(3)

    // TODO https://beyondidentity.atlassian.net/browse/ZER-7622 Move biometric/keyguard prompts in Core
    private fun ask() {
        val cancellationSignal = CancellationSignal().apply {
            setOnCancelListener { logger?.invoke("biometrics cancelled") }
        }

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                keyguardPrompt?.invoke { result, exception ->
                    if (exception == null) {
                        logger?.invoke("The exception is null for auth error")
                        answers.sendBlocking(result)
                    } else {
                        logger?.invoke("The exception is $exception")
                        // security challenge not present, cancel export and ask user to add a challenge
                        answers.sendBlocking(false)
                    }
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Called when a biometric is recognized.
                logger?.invoke("authentication is success")
                answers.sendBlocking(true)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                // Called when a biometric is valid but not recognized.
                // we don't respond with answer false here, because the user could try another biometric or choose pin.
                logger?.invoke("authentication failed")
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                super.onAuthenticationHelp(helpCode, helpString)
                logger?.invoke("authentication help with core $helpCode and message $helpString")
                answers.sendBlocking(false)
            }
        }

        BiometricPrompt.Builder(app)
            .setTitle(app.getString(R.string.embedded_export_biometric_prompt_title))
            .setNegativeButton(
                app.getString(R.string.embedded_export_biometric_prompt_cancel),
                executor,
                { _, _ ->
                    logger?.invoke("User cancelled biometric check")
                    answers.sendBlocking(false)
                }
            )
            .build()
            .authenticate(
                cancellationSignal,
                executor,
                callback,
            )
    }

    /**
     * Initialize and configure the Beyond Identity Embedded SDK.
     *
     * @param app [Application]
     * @param apiToken API token used to authenticate to Beyond Identity's register user endpoint.
     * @param keyguardPrompt If no biometrics is set, this callback should launch the keyguard service and return the answer
     * @param logger Custom logger to get logs from the SDK
     */
    fun init(
        app: Application,
        apiToken: String,
        config: Config,
        keyguardPrompt: (((allow: Boolean, exception: Exception?) -> Unit) -> Unit)?,
        logger: (String) -> Unit,
    ) {
        this.app = app
        this.apiToken = apiToken
        this.config = config
        this.keyguardPrompt = keyguardPrompt
        this.logger = logger

        BiSdk.init(
            app = app,
            isRooted = false,
            deviceSerialNumber = { "embedded-device-number" },
            ask = {
                runBlocking {
                    launch(Dispatchers.Main) { ask() }
                    answers.receive()
                }
            },
            authenticationPrompt = { true },
            appVersion = app.appVersionName(),
            appInstancePrefKey = "",
            localhostServicePrefKey = "",
            accessibilityServicePrefKey = "",
            deviceGatewayUrl = BuildConfig.BUILD_CONFIG_DEVICE_GATEWAY_URL,
            channel = BuildConfig.BUILD_CONFIG_CHANNEL,
            biLogger = object : BiLogger {
                override fun log(
                    type: LogType,
                    category: LogCategory,
                    message: String,
                    file: String,
                    method: String,
                    line: Int
                ) {
                    logger("${file.split(".").last()}\$$method\$$line | $type | $message")
                }
            },
            deviceUid = { null }
        )

        dbMigrate()
    }

    private fun newDBFile(fileName: String): String {
        val path = app.filesDir.path + File.separator + fileName
        File(path).createNewFile()
        return path
    }

    private fun dbMigrate() {
        logger?.invoke("Running DB migration")
        BiSdk.migrateDb(
            boltPath = newDBFile("auth.db"),
            sqlitePath = newDBFile("auth.sqlite")
        ) {
            when (it) {
                is CoreSuccess -> logger?.invoke("DB migration success")
                is CoreFailure -> logger?.invoke("DB migration failure")
            }
        }
    }

    /**
     * Creates PKCE authentication request params.
     * PKCE https://datatracker.ietf.org/doc/html/rfc7636
     *
     * code_verifier is high-entropy cryptographic random STRING using the
     * unreserved characters [A-Z] / [a-z] / [0-9] / "-" / "." / "_" / "~"
     * from Section 2.3 of RFC3986, with a minimum length of 43 characters
     * and a maximum length of 128 characters.
     *
     * code_challenge is function of code_verifier BASE64URL(SHA256(ASCII(code_verifier)))
     */
    fun createPkce(
        callback: (Result<PkceResponse>) -> Unit,
    ) {
        executor.execute {
            BiSdk.createPkce { pkceResponse ->
                when (pkceResponse) {
                    is CoreSuccess ->
                        postMain { callback(Result.success(PkceResponse.from(pkceResponse.value))) }
                    is CoreFailure ->
                        postMain { callback(Result.failure(Throwable(pkceResponse.value.localizedDescription))) }
                }
            }
        }
    }

    /**
     * Creates PKCE authentication request params.
     * PKCE https://datatracker.ietf.org/doc/html/rfc7636
     *
     * code_verifier is high-entropy cryptographic random STRING using the
     * unreserved characters [A-Z] / [a-z] / [0-9] / "-" / "." / "_" / "~"
     * from Section 2.3 of RFC3986, with a minimum length of 43 characters
     * and a maximum length of 128 characters.
     *
     * code_challenge is function of code_verifier BASE64URL(SHA256(ASCII(code_verifier)))
     *
     * @return [Flow] that delivers [Result] of [PkceResponse]
     */
    @ExperimentalCoroutinesApi
    fun createPkce(
        dispatcher: CoroutineDispatcher = Dispatchers.Default
    ) = callbackFlow<Result<PkceResponse>> {
        BiSdk.createPkce { pkceResponse ->
            when (pkceResponse) {
                is CoreSuccess -> sendBlocking(Result.success(PkceResponse.from(pkceResponse.value)))
                is CoreFailure -> sendBlocking(Result.failure(Throwable(pkceResponse.value.localizedDescription)))
            }
        }
    }.flowOn(dispatcher)

    /**
     * Creates a new user under tenant
     *
     * @param externalId Identifier which corresponds to the user in the app's database.
     * @param email User's email.
     * @param userName Internal field to identify the user.
     * @param displayName Internal field to identify the user's name.
     * @param callback [Result] of [UserResponse] or [Throwable]
     */
    fun createUser(
        externalId: String,
        email: String,
        displayName: String,
        userName: String,
        callback: (Result<UserResponse>) -> Unit,
    ) {
        executor.execute {
            BiSdk.createUser(
                apiToken = apiToken,
                registrationUrl = "${BuildConfig.BUILD_CONFIG_PUBLIC_API_URL}/v0/users",
                externalId = externalId,
                email = email,
                displayName = displayName,
                userName = userName,
            ) { userResponse ->
                when (userResponse) {
                    is CoreSuccess ->
                        postMain { callback(Result.success(UserResponse.from(userResponse.value))) }
                    is CoreFailure ->
                        postMain { callback(Result.failure(Throwable(userResponse.value.localizedDescription))) }
                }
            }
        }
    }

    /**
     * Creates a new user under tenant
     *
     * @param externalId Identifier which corresponds to the user in the app's database.
     * @param email User's email.
     * @param userName Internal field to identify the user.
     * @param displayName Internal field to identify the user's name.
     *
     * @return [Flow] [Result] of [UserResponse] or [Throwable]
     */
    @ExperimentalCoroutinesApi
    fun createUser(
        externalId: String,
        email: String,
        displayName: String,
        userName: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) = callbackFlow<Result<UserResponse>> {
        BiSdk.createUser(
            apiToken = apiToken,
            registrationUrl = "${BuildConfig.BUILD_CONFIG_PUBLIC_API_URL}/v0/users",
            externalId = externalId,
            email = email,
            displayName = displayName,
            userName = userName,
        ) { userResponse ->
            when (userResponse) {
                is CoreSuccess -> sendBlocking(Result.success(UserResponse.from(userResponse.value)))
                is CoreFailure -> sendBlocking(Result.failure(Throwable(userResponse.value.localizedDescription)))
            }
        }
    }.flowOn(dispatcher)

    /**
     * Recovers a user under tenant
     *
     * @param externalId Identifier which corresponds to the user in the app's database.
     * @param callback [Result] of [UserResponse]
     */
    fun recoverUser(
        externalId: String,
        callback: (Result<UserResponse>) -> Unit,
    ) {
        executor.execute {
            BiSdk.recoverUser(
                apiToken = apiToken,
                recoverUrl = "${BuildConfig.BUILD_CONFIG_PUBLIC_API_URL}/v1/manage/recover-user",
                request = UserRecoverRequest(externalId = externalId)
            ) { userResponse ->
                when (userResponse) {
                    is CoreSuccess ->
                        postMain { callback(Result.success(UserResponse.from(userResponse.value))) }
                    is CoreFailure ->
                        postMain { callback(Result.failure(Throwable(userResponse.value.localizedDescription))) }
                }
            }
        }
    }

    /**
     * Recovers a user under tenant
     *
     * @param externalId Identifier which corresponds to the user in the app's database.
     *
     * @return [Flow] [Result] of [UserResponse]
     */
    @ExperimentalCoroutinesApi
    fun recoverUser(
        externalId: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) = callbackFlow<Result<UserResponse>> {
        BiSdk.recoverUser(
            apiToken = apiToken,
            recoverUrl = "${BuildConfig.BUILD_CONFIG_PUBLIC_API_URL}/v1/manage/recover-user",
            request = UserRecoverRequest(externalId = externalId),
        ) { userResponse ->
            when (userResponse) {
                is CoreSuccess -> sendBlocking(Result.success(UserResponse.from(userResponse.value)))
                is CoreFailure -> sendBlocking(Result.failure(Throwable(userResponse.value.localizedDescription)))
            }
        }
    }.flowOn(dispatcher)

    /**
     * Use a registration link to register a credential for user
     *
     * @param url registration url used to create a credential
     * @param callback [Result] of [Credential] or [Throwable]
     */
    fun register(
        url: String,
        callback: (Result<Credential>) -> Unit,
    ) {
        executor.execute {
            BiSdk.handle(
                url = url,
                trustedSource = TrustedSource.EmbeddedSource,
            ) { urlResponse ->
                when (urlResponse) {
                    is CoreSuccess -> postMain {
                        urlResponse.value.registration?.let { registrationResponse ->
                            callback(Result.success(Credential.from(registrationResponse.profile)))
                        }
                    }
                    is CoreFailure -> postMain {
                        callback(Result.failure(Throwable(urlResponse.value.localizedDescription)))
                    }
                }
            }
        }
    }

    /**
     * Use a registration link to register a credential for user
     *
     * @param url registration url used to create a credential
     *
     * @return [Flow] with [Result] of [Credential] or [Throwable]
     */
    @ExperimentalCoroutinesApi
    fun register(
        url: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) = callbackFlow<Result<Credential>> {
        BiSdk.handle(
            url = url,
            trustedSource = TrustedSource.EmbeddedSource,
        ) { urlResponse ->
            when (urlResponse) {
                is CoreSuccess ->
                    urlResponse.value.registration?.let { registrationResponse ->
                        sendBlocking(Result.success(Credential.from(registrationResponse.profile)))
                    }
                is CoreFailure ->
                    sendBlocking(Result.failure(Throwable(urlResponse.value.localizedDescription)))
            }
        }
    }.flowOn(dispatcher)

    /**
     * Used for OIDC confidential clients
     * An app implementing the embedded sdk initiates auth request with BI.
     * This assumes the existing of a secure backend that can safely store the client secret
     * and can exchange the authorization code for an access and id token.
     *
     * @param clientId The client ID generated during the OIDC configuration.
     * @param redirectUri URI where the user will be redirected after the authorization has completed. The redirect URI must be one of the URIs passed in the OIDC configuration.
     * @param pkceS256CodeChallenge Optional but recommended to prevent authorization code injection. Use [createPkce] to generate [PkceResponse]
     * @param scope string list of OIDC scopes used during authentication to authorize access to a user's specific details. Only "openid" is currently supported.
     * @param callback returns an AuthorizationCode to exchange for access and id token.
     */
    fun authenticateConfidential(
        clientId: String,
        redirectUri: String,
        scope: String,
        pkceS256CodeChallenge: String?,
        callback: (Result<String>) -> Unit,
    ) {
        executor.execute {
            BiSdk.embeddedConfidentialOidc(
                clientId = clientId,
                authUrl = "${BuildConfig.BUILD_CONFIG_AUTH_URL}/v2/authorize",
                redirectUri = redirectUri,
                scope = scope,
                pkce = pkceS256CodeChallenge?.let { CodeChallenge(challenge = it, method = S256) },
            ) { oidcResponse ->
                when (oidcResponse) {
                    is CoreSuccess ->
                        postMain { callback(Result.success(oidcResponse.value.code)) }
                    is CoreFailure ->
                        postMain { callback(Result.failure(Throwable(oidcResponse.value.localizedDescription))) }
                }
            }
        }
    }

    /**
     * Used for OIDC confidential clients
     * An app implementing the embedded sdk initiates auth request with BI.
     * This assumes the existing of a secure backend that can safely store the client secret
     * and can exchange the authorization code for an access and id token.
     *
     * @param clientId The client ID generated during the OIDC configuration.
     * @param redirectUri URI where the user will be redirected after the authorization has completed. The redirect URI must be one of the URIs passed in the OIDC configuration.
     * @param pkceS256CodeChallenge Optional but recommended to prevent authorization code injection. Use [createPkce] to generate [PkceResponse]
     * @param scope string list of OIDC scopes used during authentication to authorize access to a user's specific details. Only "openid" is currently supported.
     *
     * @return [Flow] an AuthorizationCode to exchange for access and id token.
     */
    @ExperimentalCoroutinesApi
    fun authenticateConfidential(
        clientId: String,
        redirectUri: String,
        scope: String,
        pkceS256CodeChallenge: String?,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) = callbackFlow<Result<String>> {
        BiSdk.embeddedConfidentialOidc(
            clientId = clientId,
            authUrl = "${BuildConfig.BUILD_CONFIG_AUTH_URL}/v2/authorize",
            redirectUri = redirectUri,
            scope = scope,
            pkce = pkceS256CodeChallenge?.let { CodeChallenge(challenge = it, method = S256) },
        ) { oidcResponse ->
            when (oidcResponse) {
                is CoreSuccess -> sendBlocking(Result.success(oidcResponse.value.code))
                is CoreFailure -> sendBlocking(Result.failure(Throwable(oidcResponse.value.localizedDescription)))
            }
        }
    }.flowOn(dispatcher)

    /**
     * Used for OIDC public clients
     * An app implementing the embedded sdk initiates auth request with BI
     * This assumes there is no backend and the client secret can't be safely stored.
     * The app will get the access and id token.
     *
     * @param clientId The client ID generated during the OIDC configuration.
     * @param redirectUri URI where the user will be redirected after the authorization has completed. The redirect URI must be one of the URIs passed in the OIDC configuration.
     * @param callback returns a [TokenResponse] that contains the access and id token.
     */
    fun authenticatePublic(
        clientId: String,
        redirectUri: String,
        callback: (Result<TokenResponse>) -> Unit,
    ) {
        executor.execute {
            BiSdk.embeddedPublicOidc(
                clientId = clientId,
                authUrl = "${BuildConfig.BUILD_CONFIG_AUTH_URL}/v2/authorize",
                redirectUri = redirectUri,
                tokenUrl = "${BuildConfig.BUILD_CONFIG_AUTH_URL}/v2/token",
            ) { oidcResponse ->
                when (oidcResponse) {
                    is CoreSuccess ->
                        postMain { callback(Result.success(TokenResponse.from(oidcResponse.value))) }
                    is CoreFailure ->
                        postMain { callback(Result.failure(Throwable(oidcResponse.value.localizedDescription))) }
                }
            }
        }
    }

    /**
     * Used for OIDC public clients
     * An app implementing the embedded sdk initiates auth request with BI
     * This assumes there is no backend and the client secret can't be safely stored.
     * The app will get the access and id token.
     *
     * @param clientId The client ID generated during the OIDC configuration.
     * @param redirectUri URI where the user will be redirected after the authorization has completed. The redirect URI must be one of the URIs passed in the OIDC configuration.
     *
     * @return [Flow] [Result] of [TokenResponse] or [Throwable]
     */
    @ExperimentalCoroutinesApi
    fun authenticatePublic(
        clientId: String,
        redirectUri: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) = callbackFlow<Result<TokenResponse>> {
        BiSdk.embeddedPublicOidc(
            clientId = clientId,
            authUrl = "${BuildConfig.BUILD_CONFIG_AUTH_URL}/v2/authorize",
            redirectUri = redirectUri,
            tokenUrl = "${BuildConfig.BUILD_CONFIG_AUTH_URL}/v2/token",
        ) { oidcResponse ->
            when (oidcResponse) {
                is CoreSuccess -> sendBlocking(Result.success(TokenResponse.from(oidcResponse.value)))
                is CoreFailure -> sendBlocking(Result.failure(Throwable(oidcResponse.value.localizedDescription)))
            }
        }
    }.flowOn(dispatcher)

    /**
     * Get all current credentials for this device.
     *
     * Only one credential per device is supported currently.
     *
     * @return [List] of [Credential]
     */
    fun getCredentials(
        callback: (Result<List<Credential>>) -> Unit
    ) {
        executor.execute {
            BiSdk.allProfiles { allProfilesResult ->
                when (allProfilesResult) {
                    is CoreSuccess ->
                        postMain { callback(Result.success(allProfilesResult.value.map { Credential.from(it) })) }
                    is CoreFailure ->
                        postMain { callback(Result.failure(Throwable(allProfilesResult.value.localizedDescription))) }
                }
            }
        }
    }

    /**
     * Get all current credentials for this device.
     *
     * Only one credential per device is supported currently.
     *
     * @return [Flow] that delivers [List] of [Credential]
     */
    @ExperimentalCoroutinesApi
    fun getCredentials(
        dispatcher: CoroutineDispatcher = Dispatchers.Default
    ) = callbackFlow<Result<List<Credential>>> {
        BiSdk.allProfiles { allProfilesResult ->
            when (allProfilesResult) {
                is CoreSuccess ->
                    sendBlocking(Result.success(allProfilesResult.value.map { Credential.from(it) }))
                is CoreFailure ->
                    sendBlocking(Result.failure(Throwable(allProfilesResult.value.localizedDescription)))
            }
        }
    }.flowOn(dispatcher)

    /**
     * Delete a `Credential` by handle on current device.
     *
     * Warning: deleting a `Credential` is destructive and will remove everything from the device. If no other device contains the credential then the user will need to complete a recovery in order to log in again on this device.
     *
     * @param credentialHandle credential handle, uniquely  identifying a [Credential].
     * @param callback [Result] of [Unit]
     */
    fun deleteCredential(
        credentialHandle: String,
        callback: (Result<Unit>) -> Unit,
    ) {
        executor.execute {
            BiSdk.deleteProfile(
                handle = credentialHandle,
            ) { result ->
                when (result) {
                    is CoreSuccess ->
                        postMain { callback(Result.success(Unit)) }
                    is CoreFailure ->
                        postMain { callback(Companion.failure(Throwable(result.value.localizedDescription))) }
                }
            }
        }
    }

    /**
     * Delete a `Credential` by handle on current device.
     *
     * Warning: deleting a `Credential` is destructive and will remove everything from the device. If no other device contains the credential then the user will need to complete a recovery in order to log in again on this device.
     *
     * @param credentialHandle credential handle, uniquely  identifying a [Credential].
     * @return [Flow] that delivers [Result] of [Unit]
     */
    @ExperimentalCoroutinesApi
    fun deleteCredential(
        credentialHandle: String,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) = callbackFlow<Result<Unit>> {
        BiSdk.deleteProfile(
            handle = credentialHandle,
        ) { result ->
            when (result) {
                is CoreSuccess -> sendBlocking(Result.success(Unit))
                is CoreFailure -> sendBlocking(Companion.failure(Throwable(result.value.localizedDescription)))
            }
        }
    }.flowOn(dispatcher)

    /**
     * Export a list of credentials from one device to another.
     * The user must be in an authenticated state to export any credentials.
     *
     * During this flow the user is prompted for a biometric challenge,
     * If biometrics are not set, it falls back to pin.
     *
     * After the challenge is completed, a rendezvous token is provided
     * with 90s TTL after which a new token is generated.
     *
     * NOTE: To cancel the export flow, [EmbeddedSdk.cancel] must be invoked.
     *
     * @param credentialHandles [List] of credential handles to be exported
     */
    @ExperimentalCoroutinesApi
    fun export(
        credentialHandles: List<String>,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ): Flow<ExportResponse?> = callbackFlow {
        var error: RuntimeException? = null
        BiSdk.export(
            handles = credentialHandles,
            url = BuildConfig.BUILD_CONFIG_MIGRATED_URL,
            export = { coreExportStatus ->
                when (coreExportStatus) {
                    is CoreExportStatus.Started -> {
                        logger?.invoke("export started with token ${coreExportStatus.token}")
                        sendBlocking(
                            ExportResponse(
                                rendezvousToken = coreExportStatus.token,
                                rendezvousTokenBitmap = generateQrCode(coreExportStatus.token),
                            )
                        )
                    }
                    is CoreExportStatus.Token -> {
                        logger?.invoke("token timeout. new token ${coreExportStatus.token}")
                        sendBlocking(
                            ExportResponse(
                                rendezvousToken = coreExportStatus.token,
                                rendezvousTokenBitmap = generateQrCode(coreExportStatus.token),
                            )
                        )
                    }
                    is CoreExportStatus.Received -> {
                        logger?.invoke("requests received for the following credentials: ${coreExportStatus.handles}")
                        sendBlocking(null)
                    }
                }
            },
        ) {
            error = when (it) {
                is CoreSuccess -> null
                is CoreFailure -> RuntimeException(it.value.localizedDescription)
            }
        }

        error?.let {
            logger?.invoke("Export Credentials failed ${it.message ?: ""}")
            close(it)
        } ?: run {
            logger?.invoke("Credentials exported")
            close()
        }
    }.flowOn(dispatcher)

    /**
     * Export credentials from one device to another.
     *
     * During this flow the user is prompted for a biometric challenge,
     * If biometrics are not set, it falls back to pin.
     *
     * After the challenge is completed, a rendezvous token is provided
     * with 60s TTL after which a new token is generated.
     *
     * NOTE: To cancel the export flow, [EmbeddedSdk.cancel] must be invoked.
     *
     * @param credentialHandle [List] of credential handles to be exported
     * @param listener When biometrics are not set, fallback to pin.
     */
    fun export(
        credentialHandle: List<String>,
        listener: ExportCredentialListener,
    ) {
        executor.execute {
            BiSdk.export(
                handles = credentialHandle,
                url = BuildConfig.BUILD_CONFIG_MIGRATED_URL,
                export = { coreExportStatus ->
                    when (coreExportStatus) {
                        is CoreExportStatus.Started -> postMain {
                            logger?.invoke("export started with token ${coreExportStatus.token}")
                            listener.onUpdate(
                                ExportResponse(
                                    rendezvousToken = coreExportStatus.token,
                                    rendezvousTokenBitmap = generateQrCode(coreExportStatus.token),
                                )
                            )
                        }
                        is CoreExportStatus.Token -> postMain {
                            logger?.invoke("token timeout. new token ${coreExportStatus.token}")
                            listener.onUpdate(
                                ExportResponse(
                                    rendezvousToken = coreExportStatus.token,
                                    rendezvousTokenBitmap = generateQrCode(coreExportStatus.token),
                                )
                            )
                        }
                        is CoreExportStatus.Received -> postMain {
                            logger?.invoke("requests received for the following Credential: ${coreExportStatus.handles}")
                            listener.onUpdate(null)
                        }
                    }
                },
                callback = { result ->
                    when (result) {
                        is CoreSuccess ->
                            postMain { listener.onFinish() }
                        is CoreFailure ->
                            postMain { listener.onError(RuntimeException(result.value.localizedDescription)) }
                    }
                },
            )
        }
    }

    /**
     * Import a [Credential].
     *
     * Use this function to import a [Credential] from one device to another.
     *
     * @param token 9 digit code that the user entered generated by [EmbeddedSdk.export] on another device.
     * @param callback [Result] [List] of [Credential]
     */
    fun import(
        token: String,
        callback: (Result<List<Credential>>) -> Unit,
    ) {
        executor.execute {
            BiSdk.import(
                token = token,
                url = BuildConfig.BUILD_CONFIG_MIGRATED_URL,
            ) { importProfileResult ->
                when (importProfileResult) {
                    is CoreSuccess ->
                        postMain { callback(Result.success(importProfileResult.value.map { Credential.from(it) })) }
                    is CoreFailure ->
                        postMain { callback(Result.failure(Throwable(importProfileResult.value.localizedDescription))) }
                }
            }
        }
    }

    /**
     * Import a [Credential].
     *
     * Use this function to import a [Credential] from one device to another.
     *
     * @param token 9 digit code that the user entered generated by [EmbeddedSdk.export] on another device.
     * @return [Result] [List] of [Credential]
     */
    @ExperimentalCoroutinesApi
    fun import(
        token: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) = callbackFlow<Result<List<Credential>>> {
        BiSdk.import(
            token = token,
            url = BuildConfig.BUILD_CONFIG_MIGRATED_URL,
        ) { importProfileResult ->
            when (importProfileResult) {
                is CoreSuccess ->
                    sendBlocking(Result.success(importProfileResult.value.map { Credential.from(it) }))
                is CoreFailure ->
                    sendBlocking(Result.failure(Throwable(importProfileResult.value.localizedDescription)))
            }
        }
    }.flowOn(dispatcher)

    /**
     * Cancels ongoing requests.
     */
    fun cancel(
        callback: (Result<Unit>) -> Unit
    ) {
        BiSdk.cancel { response ->
            logger?.invoke(response.toString())
            when (response) {
                is CoreSuccess ->
                    postMain { callback(Result.success(Unit)) }
                is CoreFailure ->
                    postMain { callback(Result.failure(Throwable(response.value.localizedDescription))) }
            }
        }
    }

    /**
     * Cancels ongoing requests.
     */
    @ExperimentalCoroutinesApi
    fun cancel(
        dispatcher: CoroutineDispatcher = Dispatchers.Default
    ) = callbackFlow<Result<Unit>> {
        BiSdk.cancel { response ->
            logger?.invoke(response.toString())
            when (response) {
                is CoreSuccess -> sendBlocking(Result.success(Unit))
                is CoreFailure -> sendBlocking(Result.failure(Throwable(response.value.localizedDescription)))
            }
        }
    }.flowOn(dispatcher)

    /**
     * For certain flows (export, authenticate, etc...) the user needs to be prompted to approve
     * an action or to pass a security challenge, and when we leave the flow this method can be used
     * to provide the answer to the SDK.
     */
    fun answer(
        answer: Boolean,
    ) {
        answers.sendBlocking(answer)
    }

    fun deviceInfo() {
    }
}
