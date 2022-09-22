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
import com.beyondidentity.embedded.sdk.models.BindCredentialResponse
import com.beyondidentity.embedded.sdk.models.Credential
import com.beyondidentity.embedded.sdk.models.CredentialID
import com.beyondidentity.embedded.sdk.models.OnSelectCredential
import com.beyondidentity.embedded.sdk.models.OnSelectedCredential
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
import com.beyondidentity.sdk.android.bicore.models.CredentialHandling
import com.beyondidentity.sdk.android.bicore.models.CryptoSource
import com.beyondidentity.sdk.android.bicore.models.HostClientEnvironment
import com.beyondidentity.sdk.android.bicore.models.KeyStorageStrategy
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
import kotlin.jvm.Throws

object EmbeddedSdk {
    private const val FLOW_TYPE_EMBEDDED = "embedded"
    private const val BI_APP_INSTANCE_ID_PREF_KEY = "beyond-identity-app-instance-id"

    private var onSelectCredentialCallback: OnSelectCredential? = null
    private val selectCredentialSubject = Channel<String?>()
    private val answers = Channel<Boolean>()

    private lateinit var app: Application
    private lateinit var biometricAskPrompt: String
    private var logger: ((String) -> Unit)? = null
    private var keyguardPrompt: (((allow: Boolean, exception: Exception?) -> Unit) -> Unit)? = null
    private var hasInitalizedCore = false

    private val executor = Executors.newFixedThreadPool(3)

    // TODO https://beyondidentity.atlassian.net/browse/ZER-7622 Move biometric/keyguard prompts in Core
    private fun ask() {
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

    private fun selectCredential(credentialsToSelectFrom: List<Credential>) {
        val callback = object : OnSelectedCredential {
            override fun invoke(selectedCredentialId: CredentialID?) {
                selectCredentialSubject.trySendBlocking(selectedCredentialId)
            }
        }

        onSelectCredentialCallback?.invoke(credentialsToSelectFrom, callback)
    }

    /**
     * Initialize and configure the Beyond Identity Embedded SDK.
     *
     * @param allowedDomains Optional array of domains that we whitelist against for network operations.
     * This will default to Beyond Identity's allowed domains.
     * @param app [Application]
     * @param biometricAskPrompt A prompt the user will see when asked for biometrics while extending a credential to another device.
     * @param keyguardPrompt If no biometrics is set, this callback should launch the keyguard service and return the answer
     * @param logger Custom logger to get logs from the SDK
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

        if (hasInitalizedCore) {
            return
        }

        BiSdk.init(
            app = app,
            isRooted = false,
            deviceSerialNumber = { "embedded-device-number" },
            deviceUid = { null },
            ask = {
                runBlocking {
                    launch(Dispatchers.Main) { ask() }
                    answers.receive()
                }
            },
            authenticationPrompt = { true },
            selectCredentialPrompt = { null },
            selectAuthNCredentialPrompt = { credentials ->
                runBlocking {
                    launch(Dispatchers.Main) {
                        selectCredential(
                            credentials.map {
                                Log.info(LogCategory.Credential, Credential.from(it).toString())
                                Credential.from(it)
                            }
                        )
                    }
                    selectCredentialSubject.receive()
                }
            },
            // This is the version of the native platform authenticator. Since this SDK has nothing to do
            // with the native platform authenticator, we set this to a dummy value.
            appVersion = "0.0.0",
            appInstancePrefKey = getAppInstanceId(app),
            localhostServicePrefKey = "",
            accessibilityServicePrefKey = "",
            deviceGatewayUrl = BuildConfig.BUILD_CONFIG_DEVICE_GATEWAY_URL,
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
            ),
            locale = null,
        )

        dbMigrate(allowedDomains = if (allowedDomains.isNullOrEmpty()) listOf("beyondidentity.com") else allowedDomains)
        this.hasInitalizedCore = true
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
     * Bind a credential to this device.
     *
     * @param url URL used to bind a credential to this device
     * @param callback [Result] of [BindCredentialResponse] or [Throwable]
     */
    @JvmStatic
    fun bindCredential(
        url: String,
        callback: (Result<BindCredentialResponse>) -> Unit,
    ) {
        executor.execute {
            if (!isBindCredentialUrl(url)) {
                callback(Result.failure(Throwable("URL provided is invalid")))
            } else {
                BiSdk.bindCredential(
                    url = url,
                    trustedSource = TrustedSource.EmbeddedSource,
                    flowType = FLOW_TYPE_EMBEDDED,
                ) { bindCredentialResult ->
                    when (bindCredentialResult) {
                        is CoreSuccess -> postMain {
                            callback(Result.success(BindCredentialResponse.from(bindCredentialResult.value)))
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
     * Bind a credential to this device.
     *
     * @param url URL used to bind a credential to this device
     * @return [Flow] with [Result] of [BindCredentialResponse] or [Throwable]
     */
    fun bindCredential(
        url: String,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) = callbackFlow<Result<BindCredentialResponse>> {
        if (!isBindCredentialUrl(url)) {
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
                        trySendBlocking(Result.success(BindCredentialResponse.from(bindCredentialResult.value)))
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
     * @param credentialId The ID of the credential with which to authenticate.
     * @param callback [Result] of [AuthenticateResponse] or [Throwable]
     */
    @JvmStatic
    fun authenticate(
        url: String,
        credentialId: String,
        callback: (Result<AuthenticateResponse>) -> Unit,
    ) {
        if (!isAuthenticateUrl(url)) {
            callback(Result.failure(Throwable("URL provided is invalid")))
        } else {
            executor.execute {
                BiSdk.biAuthenticate(
                    url = url,
                    credentialId = credentialId,
                    trustedSource = TrustedSource.EmbeddedSource,
                    flowType = FLOW_TYPE_EMBEDDED,
                ) { biAuthenticateResult ->
                    when (biAuthenticateResult) {
                        is CoreSuccess -> postMain {
                            callback(Result.success(AuthenticateResponse.from(biAuthenticateResult.value)))
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
     * @param credentialId The ID of the credential with which to authenticate.
     * @return [Flow] [Result] of [AuthenticateResponse] or [Throwable]
     */
    fun authenticate(
        url: String,
        credentialId: String,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) = callbackFlow<Result<AuthenticateResponse>> {
        if (!isAuthenticateUrl(url)) {
            trySendBlocking(Result.failure(Throwable("URL provided is invalid")))
            awaitClose()
        } else {
            BiSdk.biAuthenticate(
                url = url,
                credentialId = credentialId,
                trustedSource = TrustedSource.EmbeddedSource,
                flowType = FLOW_TYPE_EMBEDDED,
            ) { biAuthenticateResult ->
                when (biAuthenticateResult) {
                    is CoreSuccess -> trySendBlocking(Result.success(AuthenticateResponse.from(biAuthenticateResult.value)))
                    is CoreFailure -> trySendBlocking(Result.failure(Throwable(biAuthenticateResult.value.localizedDescription)))
                }
            }
            awaitClose()
        }
    }.flowOn(dispatcher)

    /**
     * Get all current credentials for this device.
     *
     * @return [List] of [Credential]
     */
    @JvmStatic
    fun getCredentials(
        callback: (Result<List<Credential>>) -> Unit,
    ) {
        executor.execute {
            BiSdk.getAuthNCredentials { getAuthNCredentialsResult ->
                when (getAuthNCredentialsResult) {
                    is CoreSuccess -> postMain {
                        callback(Result.success(getAuthNCredentialsResult.value.map { Credential.from(it) }))
                    }
                    is CoreFailure -> postMain {
                        callback(Result.failure(Throwable(getAuthNCredentialsResult.value.localizedDescription)))
                    }
                }
            }
        }
    }

    /**
     * Get all current credentials for this device.
     *
     * @return [Flow] that delivers [List] of [Credential]
     */
    fun getCredentials(
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) = callbackFlow<Result<List<Credential>>> {
        BiSdk.getAuthNCredentials { getAuthNCredentialsResult ->
            when (getAuthNCredentialsResult) {
                is CoreSuccess ->
                    trySendBlocking(Result.success(getAuthNCredentialsResult.value.map { Credential.from(it) }))
                is CoreFailure ->
                    trySendBlocking(Result.failure(Throwable(getAuthNCredentialsResult.value.localizedDescription)))
            }
        }
        awaitClose()
    }.flowOn(dispatcher)

    /**
     * Delete a `Credential` by ID on current device.
     *
     * Warning: deleting a `Credential` is destructive and will remove everything from the device. If no other device contains the credential then the user will need to complete a recovery in order to log in again on this device.
     *
     * @param id credential id, uniquely identifying a [Credential].
     * @param callback [Result] of [Unit]
     */
    @JvmStatic
    fun deleteCredential(
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
     * Delete a `Credential` by ID on current device.
     *
     * Warning: deleting a `Credential` is destructive and will remove everything from the device. If no other device contains the credential then the user will need to complete a recovery in order to log in again on this device.
     *
     * @param id id, uniquely identifying a [Credential].
     * @return [Flow] that delivers [Result] of [Unit]
     */
    fun deleteCredential(
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
     * Returns whether a Url is a valid Authenticate Url or not.
     *
     * @param url A Url String
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
     * Returns whether a Url is a valid Authenticate Url or not.
     *
     * @param url A Url String
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
     * Returns whether a Url is a valid Bind Credential Url or not.
     *
     * @param url A Url String
     * @return true or false
     */
    @JvmStatic
    fun isBindCredentialUrl(
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
     * Returns whether a Url is a valid Bind Credential Url or not.
     *
     * @param url A Url String
     * @return true or false
     */
    fun isBindCredentialUrl(
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
}
