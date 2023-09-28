@file:Suppress("unused")

package com.beyondidentity.embedded.sdk

import android.app.Application
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal
import androidx.preference.PreferenceManager
import com.beyondidentity.authenticator.sdk.embedded.BuildConfig
import com.beyondidentity.authenticator.sdk.embedded.R
import com.beyondidentity.embedded.sdk.exceptions.DatabaseSetupException
import com.beyondidentity.embedded.sdk.models.AuthenticateResponse
import com.beyondidentity.embedded.sdk.models.AuthenticationContext
import com.beyondidentity.embedded.sdk.models.BindPasskeyResponse
import com.beyondidentity.embedded.sdk.models.OnSelectPasskey
import com.beyondidentity.embedded.sdk.models.OnSelectedPasskey
import com.beyondidentity.embedded.sdk.models.OtpChallengeResponse
import com.beyondidentity.embedded.sdk.models.Passkey
import com.beyondidentity.embedded.sdk.models.PasskeyId
import com.beyondidentity.embedded.sdk.models.RedeemOtpResponse
import com.beyondidentity.embedded.sdk.utils.appVersionName
import com.beyondidentity.embedded.sdk.utils.postMain
import com.beyondidentity.endpoint.android.lib.deviceinfo.DeviceInfo
import com.beyondidentity.endpoint.android.lib.log.BiLogger
import com.beyondidentity.endpoint.android.lib.log.Log
import com.beyondidentity.endpoint.android.lib.log.LogCategory
import com.beyondidentity.endpoint.android.lib.log.LogType
import com.beyondidentity.sdk.android.bicore.BiSdk
import com.beyondidentity.sdk.android.bicore.models.AuthLibConfiguration
import com.beyondidentity.sdk.android.bicore.models.AuthLibStoreConfiguration
import com.beyondidentity.sdk.android.bicore.models.BeginEmailOtp
import com.beyondidentity.sdk.android.bicore.models.CredentialHandling
import com.beyondidentity.sdk.android.bicore.models.CredentialId
import com.beyondidentity.sdk.android.bicore.models.CryptoSource
import com.beyondidentity.sdk.android.bicore.models.HostClientEnvironment
import com.beyondidentity.sdk.android.bicore.models.KeyStorageStrategy
import com.beyondidentity.sdk.android.bicore.models.RedeemOtp
import com.beyondidentity.sdk.android.bicore.models.TrustedSource
import com.beyondidentity.sdk.android.bicore.models.UrlType
import com.beyondidentity.sdk.android.bicore.partials.CoreFailure
import com.beyondidentity.sdk.android.bicore.partials.CoreSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.UUID
import java.util.concurrent.Executors
import kotlin.Result.Companion

object EmbeddedSdk {
    private const val FLOW_TYPE_EMBEDDED = "embedded"
    private const val BI_APP_INSTANCE_ID_PREF_KEY = "beyond-identity-app-instance-id"

    private var onSelectPasskeyCallback: OnSelectPasskey? = null
    private val selectPasskeySubject = Channel<String?>()
    private val answers = Channel<Boolean>()

    private var allowedDomains: List<String>? = null
    private lateinit var app: Application
    private lateinit var biometricAskPrompt: String
    private var keyguardPrompt: (((allow: Boolean, exception: Exception?) -> Unit) -> Unit)? = null
    private var logger: ((String) -> Unit)? = null
    private var hasInitializedCore = false

    private val executor = Executors.newFixedThreadPool(3)

    // TODO https://beyondidentity.atlassian.net/browse/ZER-7622 Move biometric/keyguard prompts in Core
    private fun ask(biometricOnly: Boolean = false) {
        val cancellationSignal = CancellationSignal().apply {
            setOnCancelListener { logger?.invoke("biometrics cancelled") }
        }

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                logger?.invoke("Biometric failed | errCode = $errorCode, errString = $errString")
                keyguardPrompt?.invoke { result, exception ->
                    if (exception == null) {
                        logger?.invoke("The exception is null for auth error")
                        answers.trySendBlocking(result)
                    } else {
                        logger?.invoke("The exception is $exception")
                        // security challenge not present, cancel extend and ask user to add a challenge
                        answers.trySendBlocking(false)
                    }
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Called when a biometric is recognized.
                logger?.invoke("authentication is success")
                answers.trySendBlocking(true)
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
                answers.trySendBlocking(false)
            }
        }

        BiometricPrompt.Builder(app)
            .setTitle(biometricAskPrompt)
            .setNegativeButton(
                app.getString(R.string.embedded_export_biometric_prompt_cancel),
                executor,
            ) { _, _ ->
                logger?.invoke("User cancelled biometric check")
                answers.trySendBlocking(false)
            }
            .build()
            .authenticate(
                cancellationSignal,
                executor,
                callback,
            )
    }

    private fun selectPasskey(passkeysToSelectFrom: List<Passkey>) {
        val callback = object : OnSelectedPasskey {
            override fun invoke(selectedPasskeyId: PasskeyId?) {
                selectPasskeySubject.trySendBlocking(selectedPasskeyId)
            }
        }

        onSelectPasskeyCallback?.invoke(passkeysToSelectFrom, callback)
    }

    /**
     * Initialize and configure the Beyond Identity Embedded SDK.
     *
     * @param allowedDomains Optional array of domains that we whitelist against for network operations.
     * This will default to Beyond Identity's allowed domains.
     * @param app [Application]
     * @param biometricAskPrompt A prompt the user will see when asked for biometrics while extending a passkey to another device.
     * @param keyguardPrompt If no biometrics is set, this callback should launch the keyguard service and return the answer.
     * @param logger Custom logger to get logs from the SDK.
     */
    @Throws(DatabaseSetupException::class)
    @JvmStatic
    fun init(
        app: Application,
        keyguardPrompt: (((allow: Boolean, exception: Exception?) -> Unit) -> Unit)?,
        logger: (String) -> Unit,
        biometricAskPrompt: String = app.getString(R.string.embedded_export_biometric_prompt_title),
        allowedDomains: List<String>? = null,
    ) {
        this.app = app
        this.keyguardPrompt = keyguardPrompt
        this.logger = logger
        this.biometricAskPrompt = biometricAskPrompt
        this.allowedDomains = allowedDomains

        if (hasInitializedCore) {
            return
        }

        BiSdk.init(
            app = app,
            isRooted = false,
            deviceSerialNumber = { "embedded-device-number" },
            deviceUid = { null },
            ask = { _, biometricOnly ->
                runBlocking {
                    launch(Dispatchers.Main) { ask(biometricOnly) }
                    answers.receive()
                }
            },
            authenticationPrompt = { true },
            selectCredentialPrompt = { null },
            selectAuthNCredentialPrompt = { passkeys ->
                runBlocking {
                    launch(Dispatchers.Main) {
                        selectPasskey(
                            passkeys.map {
                                Log.info(LogCategory.Passkey, Passkey.from(it).toString())
                                Passkey.from(it)
                            }
                        )
                    }
                    selectPasskeySubject.receive()
                }
            },
            // This is the version of the native platform authenticator. Since this SDK has nothing to do
            // with the native platform authenticator, we set this to a dummy value.
            appVersion = "0.0.0",
            appInstancePrefKey = getAppInstanceId(app),
            localhostServicePrefKey = "",
            accessibilityServicePrefKey = "",
            deviceGatewayUrl = BuildConfig.BUILD_CONFIG_DEVICE_GATEWAY_URL,
            unattestedEventUrl = BuildConfig.BUILD_CONFIG_UNATTESTED_EVENT_URL,
            channel = BuildConfig.BUILD_CONFIG_CHANNEL,
            biSdkInfo = DeviceInfo.BiSdkInfo(
                sdkVersion = BuildConfig.BUILD_CONFIG_BI_SDK_VERSION,
                appVersion = app.appVersionName(),
            ),
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
            clientEnvironment = HostClientEnvironment(
                cryptoSource = CryptoSource.Host,
                credentialHandling = CredentialHandling.Legacy,
                keyStorageStrategy = KeyStorageStrategy.TeeIfAvailable,
                gdcUrl = BuildConfig.BUILD_CONFIG_GDC_URL,
            ),
            locale = null,
        )

        dbMigrate(allowedDomains = if (allowedDomains.isNullOrEmpty()) listOf("beyondidentity.com") else allowedDomains)
        this.hasInitializedCore = true
    }

    private fun newDBDirectory(fileName: String): String {
        val path = app.filesDir.path + File.separator + fileName
        File(path).mkdir()
        return path
    }

    private fun newDBFile(fileName: String): String {
        val path = app.filesDir.path + File.separator + fileName
        File(path).createNewFile()
        return path
    }

    @Throws(DatabaseSetupException::class)
    private fun dbMigrate(allowedDomains: List<String>) {
        logger?.invoke("Running DB migration")
        BiSdk.migrateDb(
            allowedDomains = allowedDomains,
            sqlitePath = newDBFile("auth.sqlite"),
            authlib = AuthLibConfiguration(
                storeConfig = AuthLibStoreConfiguration(
                    directory = newDBDirectory(""),
                )
            ),
            callingAppInfo = null, // for eventing purposes
        ) {
            when (it) {
                is CoreSuccess -> logger?.invoke("DB migration success")
                is CoreFailure -> {
                    logger?.invoke("DB migration failure")
                    throw DatabaseSetupException("DB migration failure")
                }
            }
        }
    }

    /**
     * Bind a passkey to this device.
     *
     * @param url URL used to bind a passkey to this device
     * @param callback [Result] of [BindPasskeyResponse] or [Throwable]
     */
    @JvmStatic
    fun bindPasskey(
        url: String,
        callback: (Result<BindPasskeyResponse>) -> Unit,
    ) {
        executor.execute {
            if (!isBindPasskeyUrl(url)) {
                callback(Result.failure(Throwable("URL provided is invalid")))
            } else {
                BiSdk.bindCredential(
                    url = url,
                    trustedSource = TrustedSource.EmbeddedSource,
                    flowType = FLOW_TYPE_EMBEDDED,
                ) { bindCredentialResult ->
                    when (bindCredentialResult) {
                        is CoreSuccess -> postMain {
                            callback(Result.success(BindPasskeyResponse.from(bindCredentialResult.value)))
                        }
                        is CoreFailure -> postMain {
                            callback(Result.failure(Throwable(bindCredentialResult.value.localizedDescription)))
                        }
                    }
                }
            }
        }
    }

    /**
     * Bind a passkey to this device.
     *
     * @param url URL used to bind a passkey to this device
     * @return [Flow] with [Result] of [BindPasskeyResponse] or [Throwable]
     */
    fun bindPasskey(
        url: String,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) = callbackFlow<Result<BindPasskeyResponse>> {
        if (!isBindPasskeyUrl(url)) {
            trySendBlocking(Result.failure(Throwable("URL provided is invalid")))
            awaitClose()
        } else {
            BiSdk.bindCredential(
                url = url,
                trustedSource = TrustedSource.EmbeddedSource,
                flowType = FLOW_TYPE_EMBEDDED,
            ) { bindCredentialResult ->
                when (bindCredentialResult) {
                    is CoreSuccess ->
                        trySendBlocking(Result.success(BindPasskeyResponse.from(bindCredentialResult.value)))
                    is CoreFailure ->
                        trySendBlocking(Result.failure(Throwable(bindCredentialResult.value.localizedDescription)))
                }
            }
            awaitClose()
        }
    }.flowOn(dispatcher)

    /**
     * Authenticate a user.
     *
     * @param url URL used to authenticate
     * @param passkeyId The ID of the passkey with which to authenticate.
     * @param callback [Result] of [AuthenticateResponse] or [Throwable]
     */
    @JvmStatic
    fun authenticate(
        url: String,
        passkeyId: String,
        callback: (Result<AuthenticateResponse>) -> Unit,
    ) {
        if (!isAuthenticateUrl(url)) {
            callback(Result.failure(Throwable("URL provided is invalid")))
        } else {
            executor.execute {
                BiSdk.biAuthenticate(
                    url = url,
                    trustedSource = TrustedSource.EmbeddedSource,
                    flowType = FLOW_TYPE_EMBEDDED,
                    credentialDescriptor = CredentialId(passkeyId),
                ) { biAuthenticateResult ->
                    when (biAuthenticateResult) {
                        is CoreSuccess -> postMain {
                            callback(Result.success(AuthenticateResponse.from(biAuthenticateResult.value)!!))
                        }
                        is CoreFailure -> postMain {
                            callback(Result.failure(Throwable(biAuthenticateResult.value.localizedDescription)))
                        }
                    }
                }
            }
        }
    }

    /**
     * Authenticate a user.
     *
     * @param url URL used to authenticate
     * @param passkeyId The ID of the passkey with which to authenticate.
     * @return [Flow] [Result] of [AuthenticateResponse] or [Throwable]
     */
    fun authenticate(
        url: String,
        passkeyId: String,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) = callbackFlow<Result<AuthenticateResponse>> {
        if (!isAuthenticateUrl(url)) {
            trySendBlocking(Result.failure(Throwable("URL provided is invalid")))
            awaitClose()
        } else {
            BiSdk.biAuthenticate(
                url = url,
                trustedSource = TrustedSource.EmbeddedSource,
                flowType = FLOW_TYPE_EMBEDDED,
                credentialDescriptor = CredentialId(passkeyId),
            ) { biAuthenticateResult ->
                when (biAuthenticateResult) {
                    is CoreSuccess -> trySendBlocking(Result.success(AuthenticateResponse.from(biAuthenticateResult.value)!!))
                    is CoreFailure -> trySendBlocking(Result.failure(Throwable(biAuthenticateResult.value.localizedDescription)))
                }
            }
            awaitClose()
        }
    }.flowOn(dispatcher)

    /**
     * Get all current passkeys for this device.
     *
     * @return [List] of [Passkey]
     */
    @JvmStatic
    fun getPasskeys(
        callback: (Result<List<Passkey>>) -> Unit,
    ) {
        executor.execute {
            BiSdk.getAuthNCredentials { getAuthNCredentialsResult ->
                when (getAuthNCredentialsResult) {
                    is CoreSuccess -> postMain {
                        callback(Result.success(getAuthNCredentialsResult.value.map { Passkey.from(it) }))
                    }
                    is CoreFailure -> postMain {
                        callback(Result.failure(Throwable(getAuthNCredentialsResult.value.localizedDescription)))
                    }
                }
            }
        }
    }

    /**
     * Get all current passkeys for this device.
     *
     * @return [Flow] that delivers [List] of [Passkey]
     */
    fun getPasskeys(
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) = callbackFlow<Result<List<Passkey>>> {
        BiSdk.getAuthNCredentials { getAuthNCredentialsResult ->
            when (getAuthNCredentialsResult) {
                is CoreSuccess ->
                    trySendBlocking(Result.success(getAuthNCredentialsResult.value.map { Passkey.from(it) }))
                is CoreFailure ->
                    trySendBlocking(Result.failure(Throwable(getAuthNCredentialsResult.value.localizedDescription)))
            }
        }
        awaitClose()
    }.flowOn(dispatcher)

    /**
     * Delete a [Passkey] by ID on current device.
     *
     * Note: It is possible to delete a passkey that does not exist.
     * Warning: deleting a [Passkey] is destructive and will remove everything from the device. If no other device contains the passkey then the user will need to complete a recovery in order to log in again on this device.
     *
     * @param id the unique identifier of the [Passkey].
     * @param callback [Result] of [Unit]
     */
    @JvmStatic
    fun deletePasskey(
        id: String,
        callback: (Result<Unit>) -> Unit,
    ) {
        executor.execute {
            BiSdk.deleteAuthNCredential(
                id = id,
            ) { result ->
                when (result) {
                    is CoreSuccess -> postMain {
                        callback(Result.success(Unit))
                    }
                    is CoreFailure -> postMain {
                        callback(Companion.failure(Throwable(result.value.localizedDescription)))
                    }
                }
            }
        }
    }

    /**
     * Delete a [Passkey] by ID on current device.
     *
     * Warning: deleting a [Passkey] is destructive and will remove everything from the device. If no other device contains the passkey then the user will need to complete a recovery in order to log in again on this device.
     *
     * @param id the unique identifier of the [Passkey].
     * @return [Flow] that delivers [Result] of [Unit]
     */
    fun deletePasskey(
        id: String,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) = callbackFlow<Result<Unit>> {
        BiSdk.deleteAuthNCredential(
            id = id,
        ) { result ->
            when (result) {
                is CoreSuccess -> trySendBlocking(Result.success(Unit))
                is CoreFailure -> trySendBlocking(Companion.failure(Throwable(result.value.localizedDescription)))
            }
        }
        awaitClose()
    }.flowOn(dispatcher)

    /**
     * For certain flows (export, authenticate, etc...) the user needs to be prompted to approve
     * an action or to pass a security challenge, and when we leave the flow this method can be used
     * to provide the answer to the SDK.
     */
    fun answer(
        answer: Boolean,
    ) {
        answers.trySendBlocking(answer)
    }

    private fun getAppInstanceId(app: Application): String {
        val pref = PreferenceManager.getDefaultSharedPreferences(app)
        pref.getString(BI_APP_INSTANCE_ID_PREF_KEY, null)?.let {
            return it
        } ?: run {
            val appId = UUID.randomUUID().toString()
            pref.edit().apply {
                putString(BI_APP_INSTANCE_ID_PREF_KEY, appId)
                apply()
            }
            return appId
        }
    }

    /**
     * Returns whether a URL is a valid Authenticate URL or not.
     *
     * @param url A URL String
     * @return true or false
     */
    @JvmStatic
    fun isAuthenticateUrl(
        url: String,
    ): Boolean {
        val it = BiSdk.getUrlType(
            url = url,
        )
        return when (it) {
            is CoreSuccess -> it.value == UrlType.Authenticate
            is CoreFailure -> false
        }
    }

    /**
     * Returns whether a URL is a valid Authenticate URL or not.
     *
     * @param url A URL String
     * @return true or false
     */
    fun isAuthenticateUrl(
        url: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) = callbackFlow {
        val it = BiSdk.getUrlType(
            url = url,
        )
        when (it) {
            is CoreSuccess -> trySendBlocking(it.value == UrlType.Authenticate)
            is CoreFailure -> trySendBlocking(false)
        }
        awaitClose()
    }.flowOn(dispatcher)

    /**
     * Returns whether a URL is a valid Bind Passkey URL or not.
     *
     * @param url A URL String
     * @return true or false
     */
    @JvmStatic
    fun isBindPasskeyUrl(
        url: String,
    ): Boolean {
        val it = BiSdk.getUrlType(
            url = url,
        )
        return when (it) {
            is CoreSuccess -> it.value == UrlType.Bind
            is CoreFailure -> false
        }
    }

    /**
     * Returns whether a URL is a valid Bind Passkey URL or not.
     *
     * @param url A URL String
     * @return true or false
     */
    fun isBindPasskeyUrl(
        url: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) = callbackFlow {
        val it = BiSdk.getUrlType(
            url = url,
        )
        when (it) {
            is CoreSuccess -> trySendBlocking(it.value == UrlType.Bind)
            is CoreFailure -> trySendBlocking(false)
        }
        awaitClose()
    }.flowOn(dispatcher)

    /**
     * Returns the Authentication Context for the current transaction.
     *
     * The Authentication Context contains the Authenticator Config,
     * Authentication Method Configuration, request origin, and the
     * authenticating application.
     *
     * @param url The authentication URL of the current transaction.
     * @param callback [AuthenticationContext] or [Throwable]
     */
    @JvmStatic
    fun getAuthenticationContext(
        url: String,
        callback: (Result<AuthenticationContext>) -> Unit,
    ) {
        if (!isAuthenticateUrl(url)) {
            callback(Result.failure(Throwable("URL provided is invalid")))
        } else {
            executor.execute {
                BiSdk.getAuthenticationContext(
                    url = url,
                    allowedDomains = if (allowedDomains.isNullOrEmpty()) listOf("beyondidentity.com") else allowedDomains,
                ) { biAuthenticationContext ->
                    when (biAuthenticationContext) {
                        is CoreSuccess -> postMain {
                            callback(Result.success(AuthenticationContext.from(biAuthenticationContext.value)))
                        }
                        is CoreFailure -> postMain {
                            callback(Result.failure(Throwable(biAuthenticationContext.value.localizedDescription)))
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the Authentication Context for the current transaction.
     *
     * The Authentication Context contains the Authenticator Config,
     * Authentication Method Configuration, request origin, and the
     * authenticating application.
     *
     * @param url The authentication URL of the current transaction.
     * @return [Flow] [AuthenticationContext] or [Throwable]
     */
    fun getAuthenticationContext(
        url: String,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) = callbackFlow<Result<AuthenticationContext>> {
        if (!isAuthenticateUrl(url)) {
            trySendBlocking(Result.failure(Throwable("URL provided is invalid")))
            awaitClose()
        } else {
            BiSdk.getAuthenticationContext(
                url = url,
                allowedDomains = if (allowedDomains.isNullOrEmpty()) listOf("beyondidentity.com") else allowedDomains,
            ) { biAuthenticationContext ->
                when (biAuthenticationContext) {
                    is CoreSuccess -> trySendBlocking(Result.success(AuthenticationContext.from(biAuthenticationContext.value)))
                    is CoreFailure -> trySendBlocking(Result.failure(Throwable(biAuthenticationContext.value.localizedDescription)))
                }
            }
            awaitClose()
        }
    }.flowOn(dispatcher)

    /**
     * Initiates authentication using an OTP, which will be sent to the
     * provided email address.
     * @param url The authentication URL of the current transaction.
     * @param email The email address where the OTP will be sent.
     * @param callback [Result] of [OtpChallengeResponse] or [Throwable]
     */
    @JvmStatic
    fun authenticateOtp(
        url: String,
        email: String,
        callback: (Result<OtpChallengeResponse>) -> Unit,
    ) {
        if (!isAuthenticateUrl(url)) {
            callback(Result.failure(Throwable("URL provided is invalid")))
        } else {
            executor.execute {
                BiSdk.biAuthenticate(
                    url = url,
                    trustedSource = TrustedSource.EmbeddedSource,
                    flowType = FLOW_TYPE_EMBEDDED,
                    credentialDescriptor = BeginEmailOtp(email),
                ) { biAuthenticateResult ->
                    when (biAuthenticateResult) {
                        is CoreSuccess -> postMain {
                            callback(Result.success(OtpChallengeResponse.from(biAuthenticateResult.value)!!))
                        }
                        is CoreFailure -> postMain {
                            callback(Result.failure(Throwable(biAuthenticateResult.value.localizedDescription)))
                        }
                    }
                }
            }
        }
    }

    /**
     * Initiates authentication using an OTP, which will be sent to the
     * provided email address.
     * @param url The authentication URL of the current transaction.
     * @param email The email address where the OTP will be sent.
     * @return [Flow] [Result] of [OtpChallengeResponse] or [Throwable]
     */
    fun authenticateOtp(
        url: String,
        email: String,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) = callbackFlow<Result<OtpChallengeResponse>> {
        if (!isAuthenticateUrl(url)) {
            trySendBlocking(Result.failure(Throwable("URL provided is invalid")))
            awaitClose()
        } else {
            BiSdk.biAuthenticate(
                url = url,
                trustedSource = TrustedSource.EmbeddedSource,
                flowType = FLOW_TYPE_EMBEDDED,
                credentialDescriptor = BeginEmailOtp(email),
            ) { biAuthenticateResult ->
                when (biAuthenticateResult) {
                    is CoreSuccess -> trySendBlocking(Result.success(OtpChallengeResponse.from(biAuthenticateResult.value)!!))
                    is CoreFailure -> trySendBlocking(Result.failure(Throwable(biAuthenticateResult.value.localizedDescription)))
                }
            }
            awaitClose()
        }
    }.flowOn(dispatcher)

    /**
     * Redeems an OTP for a grant code.
     * @param url The authentication URL of the current transaction.
     * @param otp The OTP to redeem.
     * @param callback [Result] of [RedeemOtpResponse] that resolves to an [AuthenticateResponse] on success or an [OtpChallengeResponse] on failure to authenticate with the provided OTP code. Use on retry. or [Throwable].
     */
    @JvmStatic
    fun redeemOtp(
        url: String,
        otp: String,
        callback: (Result<RedeemOtpResponse>) -> Unit,
    ) {
        executor.execute {
            BiSdk.biAuthenticate(
                url = url,
                trustedSource = TrustedSource.EmbeddedSource,
                flowType = FLOW_TYPE_EMBEDDED,
                credentialDescriptor = RedeemOtp(otp),
            ) { biAuthenticateResult ->
                when (biAuthenticateResult) {
                    is CoreSuccess -> postMain {
                        biAuthenticateResult.value.allow?.let { biAuthenticateResponse ->
                            callback(Result.success(RedeemOtpResponse.Success(AuthenticateResponse.from(biAuthenticateResponse))))
                        }
                        biAuthenticateResult.value.`continue`?.let { biContinueResponse ->
                            callback(Result.success(RedeemOtpResponse.FailedOtp(OtpChallengeResponse.from(biContinueResponse))))
                        }
                    }
                    is CoreFailure -> postMain {
                        callback(Result.failure(Throwable(biAuthenticateResult.value.localizedDescription)))
                    }
                }
            }
        }
    }

    /**
     * Redeems an OTP for a grant code.
     * @param url The authentication URL of the current transaction.
     * @param otp The OTP to redeem.
     * @return [Flow] [Result] of [RedeemOtpResponse] that resolves to an [AuthenticateResponse] on success or an [OtpChallengeResponse] on failure to authenticate with the provided OTP code. Use on retry. or [Throwable].
     */
    fun redeemOtp(
        url: String,
        otp: String,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) = callbackFlow<Result<RedeemOtpResponse>> {
        BiSdk.biAuthenticate(
            url = url,
            trustedSource = TrustedSource.EmbeddedSource,
            flowType = FLOW_TYPE_EMBEDDED,
            credentialDescriptor = RedeemOtp(otp),
        ) { biAuthenticateResult ->
            when (biAuthenticateResult) {
                is CoreSuccess -> {
                    biAuthenticateResult.value.allow?.let { biAuthenticateResponse ->
                        trySendBlocking(Result.success(RedeemOtpResponse.Success(AuthenticateResponse.from(biAuthenticateResponse))))
                    }
                    biAuthenticateResult.value.`continue`?.let { biContinueResponse ->
                        trySendBlocking(Result.success(RedeemOtpResponse.FailedOtp(OtpChallengeResponse.from(biContinueResponse))))
                    }
                }
                is CoreFailure -> trySendBlocking(Result.failure(Throwable(biAuthenticateResult.value.localizedDescription)))
            }
        }
        awaitClose()
    }.flowOn(dispatcher)
}
