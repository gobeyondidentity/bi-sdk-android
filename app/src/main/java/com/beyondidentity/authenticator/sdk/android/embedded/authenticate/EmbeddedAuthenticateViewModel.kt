@file:Suppress("ktlint:standard:max-line-length")

package com.beyondidentity.authenticator.sdk.android.embedded.authenticate

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback as AuthenticationCallback
import com.auth0.android.request.DefaultClient
import com.auth0.android.result.Credentials
import com.beyondidentity.authenticator.sdk.android.apis.Auth0RetrofitBuilder
import com.beyondidentity.authenticator.sdk.android.apis.BeyondIdentityRetrofitBuilder
import com.beyondidentity.authenticator.sdk.android.apis.OktaRetrofitBuilder
import com.beyondidentity.authenticator.sdk.android.configs.Auth0Config
import com.beyondidentity.authenticator.sdk.android.configs.BeyondIdentityConfig
import com.beyondidentity.authenticator.sdk.android.configs.OktaConfig
import com.beyondidentity.authenticator.sdk.android.embedded.auth.SelectPasskeyDialogFragment
import com.beyondidentity.authenticator.sdk.android.embedded.authenticate.EmbeddedAuthenticateEvents.AuthenticateEvent
import com.beyondidentity.authenticator.sdk.android.embedded.customtab.EmbeddedCustomTabActivity
import com.beyondidentity.authenticator.sdk.android.embedded.utils.Auth0TokenFailureCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.Auth0TokenSuccessCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.AuthenticateErrorCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.AuthenticateFailureCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.AuthenticateSuccessCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.Callback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.OktaTokenFailureCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.OktaTokenSuccessCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.UpdateStateCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.resetResult
import com.beyondidentity.authenticator.sdk.android.embedded.webview.EmbeddedWebViewActivity
import com.beyondidentity.authenticator.sdk.android.utils.IntentConstants
import com.beyondidentity.authenticator.sdk.android.utils.PKCEUtil
import com.beyondidentity.authenticator.sdk.android.utils.ResponseUtil
import com.beyondidentity.authenticator.sdk.android.utils.toIndentString
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import com.beyondidentity.embedded.sdk.models.OnSelectedPasskey
import com.beyondidentity.embedded.sdk.models.Passkey
import com.okta.oidc.AuthenticationPayload
import com.okta.oidc.AuthorizationStatus
import com.okta.oidc.CustomConfiguration
import com.okta.oidc.OIDCConfig
import com.okta.oidc.Okta.WebAuthBuilder
import com.okta.oidc.ResultCallback
import com.okta.oidc.storage.SharedPreferenceStorage
import com.okta.oidc.util.AuthorizationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class EmbeddedAuthenticateViewModel : ViewModel() {
    enum class WebMode {
        CustomTab,
        Intent,
        WebView
    }

    val mWebMode = WebMode.CustomTab

    @Suppress("UNUSED_PARAMETER")
    fun handleIntent(activity: FragmentActivity, intent: Intent?) {
        intent?.data?.let { data ->
            Timber.d("handleIntent($data)")
            when {
                EmbeddedSdk.isAuthenticateUrl(data.toString()) -> {
                    onAuthenticate(
                        activity = activity,
                        url = data.toString(),
                        onAuthenticateSuccess = { success ->
                            startActivityForWebMode(
                                activity,
                                success.redirectUrl,
                                onButtonPressedRequestCode()
                            )
                        },
                        updateStateCallback = object : UpdateStateCallback {
                            override fun invoke(url: String, result: String, progress: Boolean) {
                                onButtonPressedActionHandler(
                                    onBeyondIdentity = {
                                        state = state.copy(
                                            authenticateBeyondIdentityResult = result,
                                            authenticateBeyondIdentityProgress = progress
                                        )
                                    },
                                    onAuth0SDK = {
                                        state = state.copy(
                                            authenticateAuth0SDKResult = result,
                                            authenticateAuth0SDKProgress = progress
                                        )
                                    },
                                    onAuth0Web = {
                                        state = state.copy(
                                            authenticateAuth0WebResult = result,
                                            authenticateAuth0WebProgress = progress
                                        )
                                    },
                                    onOktaSDK = {
                                        state = state.copy(
                                            authenticateOktaSDKResult = result,
                                            authenticateOktaSDKProgress = progress
                                        )
                                    },
                                    onOktaWeb = {
                                        state = state.copy(
                                            authenticateOktaWebResult = result,
                                            authenticateOktaWebProgress = progress
                                        )
                                    },
                                    onException = {
                                        throw Throwable("Invalid Button Pressed")
                                    }
                                )
                            }
                        }
                    )
                }
                Auth0Config.isRedirectUri(data) -> {
                    onButtonPressedActionHandler(
                        onAuth0SDK = {
                            val auth0 = Auth0(
                                Auth0Config.CLIENT_ID,
                                Auth0Config.DOMAIN
                            )
                            val netClient = DefaultClient(
                                enableLogging = true
                            )
                            auth0.networkingClient = netClient

                            val authenticationAPIClient = AuthenticationAPIClient(auth0)
                            authenticationAPIClient.token(
                                data.getQueryParameter("code") ?: "",
                                state.codeVerifier,
                                data.toString()
                            ).start(
                                object :
                                    AuthenticationCallback<Credentials, AuthenticationException> {
                                    // Called when there is an authentication failure
                                    override fun onFailure(error: AuthenticationException) {
                                        Timber.d("onFailure($error)")
                                        state = state.copy(
                                            authenticateAuth0SDKResult = "{\"status\":onFailure,\"error\":{\"code\":${error.getCode()},\"description\":${error.getDescription()},\"statusCode\":${error.statusCode}}}".toIndentString(
                                                includeSpace = true
                                            ),
                                            authenticateAuth0SDKProgress = false
                                        )
                                    }

                                    // Called when authentication completed successfully
                                    override fun onSuccess(result: Credentials) {
                                        Timber.d("onSuccess($result)")
                                        state = state.copy(
                                            authenticateAuth0SDKResult = "{\"status\":onSuccess,\"result\":{\"idToken\":${result.idToken},\"accessToken\":${result.accessToken},\"type\":${result.type},\"refreshToken\":${result.refreshToken},\"expiresAt\":${result.expiresAt},\"scope\":${result.scope}}}".toIndentString(
                                                includeSpace = true
                                            ),
                                            authenticateAuth0SDKProgress = false
                                        )
                                    }
                                }
                            )
                        },
                        onAuth0Web = {
                            onAuth0TokenEndpoint(
                                code = data.getQueryParameter("code") ?: "",
                                codeVerifier = state.codeVerifier,
                                redirectUri = Auth0Config.WEB_REDIRECT_URI,
                                updateStateCallback = object : UpdateStateCallback {
                                    override fun invoke(url: String, result: String, progress: Boolean) {
                                        state = state.copy(
                                            authenticateAuth0WebResult = result,
                                            authenticateAuth0WebProgress = progress
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
                OktaConfig.isRedirectUri(data) -> {
                    onButtonPressedActionHandler(
                        onOktaSDK = {
                            onOktaTokenEndpoint(
                                code = data.getQueryParameter("code") ?: "",
                                codeVerifier = state.codeVerifier,
                                redirectUri = OktaConfig.REDIRECT_URI,
                                updateStateCallback = object : UpdateStateCallback {
                                    override fun invoke(url: String, result: String, progress: Boolean) {
                                        state = state.copy(
                                            authenticateOktaSDKResult = result,
                                            authenticateOktaSDKProgress = progress
                                        )
                                    }
                                }
                            )
                        },
                        onOktaWeb = {
                            onOktaTokenEndpoint(
                                code = data.getQueryParameter("code") ?: "",
                                codeVerifier = state.codeVerifier,
                                redirectUri = OktaConfig.WEB_REDIRECT_URI,
                                updateStateCallback = object : UpdateStateCallback {
                                    override fun invoke(url: String, result: String, progress: Boolean) {
                                        state = state.copy(
                                            authenticateOktaWebResult = result,
                                            authenticateOktaWebProgress = progress
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun handleActivityResult(activity: FragmentActivity, requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            IntentConstants.AUTH0_SDK_RC,
            IntentConstants.AUTH0_WEB_RC,
            IntentConstants.OKTA_SDK_RC,
            IntentConstants.OKTA_WEB_RC,
            IntentConstants.BEYOND_IDENTITY_RC
            -> {
                data?.data?.let { uri ->
                    when {
                        EmbeddedSdk.isAuthenticateUrl(uri.toString()) -> {
                            onAuthenticate(
                                activity = activity,
                                url = uri.toString(),
                                onAuthenticateSuccess = { success ->
                                    if (!success.redirectUrl.startsWith(BeyondIdentityConfig.REDIRECT_URI)) {
                                        startActivityForWebMode(
                                            activity,
                                            success.redirectUrl,
                                            requestCode
                                        )
                                    }
                                },
                                updateStateCallback = object : UpdateStateCallback {
                                    override fun invoke(url: String, result: String, progress: Boolean) {
                                        when (requestCode) {
                                            IntentConstants.AUTH0_SDK_RC -> {
                                                state = state.copy(
                                                    authenticateAuth0SDKResult = result,
                                                    authenticateAuth0SDKProgress = progress
                                                )
                                            }
                                            IntentConstants.AUTH0_WEB_RC -> {
                                                state = state.copy(
                                                    authenticateAuth0WebResult = result,
                                                    authenticateAuth0WebProgress = progress
                                                )
                                            }
                                            IntentConstants.OKTA_SDK_RC -> {
                                                state = state.copy(
                                                    authenticateOktaSDKResult = result,
                                                    authenticateOktaSDKProgress = progress
                                                )
                                            }
                                            IntentConstants.OKTA_WEB_RC -> {
                                                state = state.copy(
                                                    authenticateOktaWebResult = result,
                                                    authenticateOktaWebProgress = progress
                                                )
                                            }
                                            IntentConstants.BEYOND_IDENTITY_RC -> {
                                                state = state.copy(
                                                    authenticateBeyondIdentityResult = result,
                                                    authenticateBeyondIdentityProgress = progress
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                        }
                        Auth0Config.isRedirectUri(uri) -> {
                            onButtonPressedActionHandler(
                                onAuth0SDK = {
                                    val auth0 = Auth0(
                                        Auth0Config.CLIENT_ID,
                                        Auth0Config.DOMAIN
                                    )
                                    val netClient = DefaultClient(
                                        enableLogging = true
                                    )
                                    auth0.networkingClient = netClient

                                    val authenticationAPIClient = AuthenticationAPIClient(auth0)
                                    authenticationAPIClient.token(
                                        uri.getQueryParameter("code") ?: "",
                                        state.codeVerifier,
                                        uri.toString()
                                    ).start(
                                        object :
                                            AuthenticationCallback<Credentials, AuthenticationException> {
                                            // Called when there is an authentication failure
                                            override fun onFailure(error: AuthenticationException) {
                                                Timber.d("onFailure($error)")
                                                when (requestCode) {
                                                    IntentConstants.AUTH0_SDK_RC -> {
                                                        state = state.copy(
                                                            authenticateAuth0SDKResult = "{\"status\":onFailure,\"error\":{\"code\":${error.getCode()},\"description\":${error.getDescription()},\"statusCode\":${error.statusCode}}}".toIndentString(
                                                                includeSpace = true
                                                            ),
                                                            authenticateAuth0SDKProgress = false
                                                        )
                                                    }
                                                    IntentConstants.AUTH0_WEB_RC -> {
                                                        state = state.copy(
                                                            authenticateAuth0WebResult = "{\"status\":onFailure,\"error\":{\"code\":${error.getCode()},\"description\":${error.getDescription()},\"statusCode\":${error.statusCode}}}".toIndentString(
                                                                includeSpace = true
                                                            ),
                                                            authenticateAuth0WebProgress = false
                                                        )
                                                    }
                                                }
                                            }

                                            // Called when authentication completed successfully
                                            override fun onSuccess(result: Credentials) {
                                                Timber.d("onSuccess($result)")
                                                when (requestCode) {
                                                    IntentConstants.AUTH0_SDK_RC -> {
                                                        state = state.copy(
                                                            authenticateAuth0SDKResult = "{\"status\":onSuccess,\"result\":{\"idToken\":${result.idToken},\"accessToken\":${result.accessToken},\"type\":${result.type},\"refreshToken\":${result.refreshToken},\"expiresAt\":${result.expiresAt},\"scope\":${result.scope}}}".toIndentString(
                                                                includeSpace = true
                                                            ),
                                                            authenticateAuth0SDKProgress = false
                                                        )
                                                    }
                                                    IntentConstants.AUTH0_WEB_RC -> {
                                                        state = state.copy(
                                                            authenticateAuth0WebResult = "{\"status\":onSuccess,\"result\":{\"idToken\":${result.idToken},\"accessToken\":${result.accessToken},\"type\":${result.type},\"refreshToken\":${result.refreshToken},\"expiresAt\":${result.expiresAt},\"scope\":${result.scope}}}".toIndentString(
                                                                includeSpace = true
                                                            ),
                                                            authenticateAuth0WebProgress = false
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    )
                                },
                                onAuth0Web = {
                                    onAuth0TokenEndpoint(
                                        code = uri.getQueryParameter("code") ?: "",
                                        codeVerifier = state.codeVerifier,
                                        redirectUri = Auth0Config.WEB_REDIRECT_URI,
                                        updateStateCallback = object : UpdateStateCallback {
                                            override fun invoke(url: String, result: String, progress: Boolean) {
                                                when (requestCode) {
                                                    IntentConstants.AUTH0_SDK_RC -> {
                                                        state = state.copy(
                                                            authenticateAuth0SDKResult = result,
                                                            authenticateAuth0SDKProgress = progress
                                                        )
                                                    }
                                                    IntentConstants.AUTH0_WEB_RC -> {
                                                        state = state.copy(
                                                            authenticateAuth0WebResult = result,
                                                            authenticateAuth0WebProgress = progress
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                            )
                        }
                        OktaConfig.isRedirectUri(uri) -> {
                            onButtonPressedActionHandler(
                                onOktaSDK = {
                                    onOktaTokenEndpoint(
                                        code = uri.getQueryParameter("code") ?: "",
                                        codeVerifier = state.codeVerifier,
                                        redirectUri = OktaConfig.REDIRECT_URI,
                                        updateStateCallback = object : UpdateStateCallback {
                                            override fun invoke(url: String, result: String, progress: Boolean) {
                                                when (requestCode) {
                                                    IntentConstants.OKTA_SDK_RC -> {
                                                        state = state.copy(
                                                            authenticateOktaSDKResult = result,
                                                            authenticateOktaSDKProgress = progress
                                                        )
                                                    }
                                                    IntentConstants.OKTA_WEB_RC -> {
                                                        state = state.copy(
                                                            authenticateOktaWebResult = result,
                                                            authenticateOktaWebProgress = progress
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    )
                                },
                                onOktaWeb = {
                                    onOktaTokenEndpoint(
                                        code = uri.getQueryParameter("code") ?: "",
                                        codeVerifier = state.codeVerifier,
                                        redirectUri = OktaConfig.WEB_REDIRECT_URI,
                                        updateStateCallback = object : UpdateStateCallback {
                                            override fun invoke(url: String, result: String, progress: Boolean) {
                                                when (requestCode) {
                                                    IntentConstants.OKTA_SDK_RC -> {
                                                        state = state.copy(
                                                            authenticateOktaSDKResult = result,
                                                            authenticateOktaSDKProgress = progress
                                                        )
                                                    }
                                                    IntentConstants.OKTA_WEB_RC -> {
                                                        state = state.copy(
                                                            authenticateOktaWebResult = result,
                                                            authenticateOktaWebProgress = progress
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e("Caught $exception")
    }

    var state: EmbeddedAuthenticateState by mutableStateOf(EmbeddedAuthenticateState())
        private set

    private val _events = MutableSharedFlow<EmbeddedAuthenticateEvents>()
    val events: SharedFlow<EmbeddedAuthenticateEvents> = _events

    fun onAuthenticateUrlTextChange(text: String) {
        state = state.copy(authenticateUrl = text)
    }

    fun onAuthenticateEmailOtpTextChange(text: String) {
        state = state.copy(authenticateEmailOtpUrl = text)
    }

    fun onRedeemEmailOtpTextChange(text: String) {
        state = state.copy(redeemEmailOtpUrl = text)
    }

    @Suppress("DEPRECATION")
    fun startActivityForWebMode(
        activity: FragmentActivity,
        url: String,
        requestCode: Int,
        webMode: WebMode = mWebMode
    ) {
        when (webMode) {
            WebMode.CustomTab -> {
                activity.startActivityForResult(
                    Intent(
                        activity,
                        EmbeddedCustomTabActivity::class.java
                    ).setData(
                        url.toUri()
                    ),
                    requestCode
                )
            }
            WebMode.Intent -> {
                activity.startActivityForResult(
                    Intent(
                        Intent.ACTION_VIEW,
                        url.toUri()
                    ).setData(
                        url.toUri()
                    ),
                    requestCode
                )
            }
            WebMode.WebView -> {
                activity.startActivityForResult(
                    Intent(
                        activity,
                        EmbeddedWebViewActivity::class.java
                    ).setData(
                        url.toUri()
                    ),
                    requestCode
                )
            }
        }
    }

    private fun onSelectPasskey(
        activity: FragmentActivity,
        passkeys: List<Passkey>,
        selectedPasskeyCallback: OnSelectedPasskey
    ) = when {
        passkeys.isEmpty() -> selectedPasskeyCallback.invoke(null)
        passkeys.size == 1 -> selectedPasskeyCallback.invoke(passkeys[0].id)
        else -> SelectPasskeyDialogFragment.newInstance(passkeys, selectedPasskeyCallback)
            .show(activity.supportFragmentManager, SelectPasskeyDialogFragment.TAG)
    }

    fun onAuthenticate(
        activity: FragmentActivity,
        url: String,
        onAuthenticateSuccess: AuthenticateSuccessCallback? = null,
        onAuthenticateFailure: AuthenticateFailureCallback? = null,
        onAuthenticateError: AuthenticateErrorCallback? = null,
        updateStateCallback: UpdateStateCallback = object : UpdateStateCallback {
            override fun invoke(url: String, result: String, progress: Boolean) {
                state = state.copy(
                    authenticateUrl = url,
                    authenticateResult = result,
                    authenticateProgress = progress
                )
            }
        }
    ) {
        if (!resetResult(
                string = url,
                result = "Please provide an Authenticate URL",
                updateStateCallback = updateStateCallback::invoke
            )
        ) {
            return
        }

        EmbeddedSdk.getPasskeys { passkeyListResult ->
            passkeyListResult.onSuccess { passkeyList ->
                onSelectPasskey(activity, passkeyList) {
                    EmbeddedSdk.authenticate(
                        url = url,
                        passkeyId = it ?: ""
                    )
                        .flowOn(Dispatchers.Main + coroutineExceptionHandler)
                        .onEach { result ->
                            result.onSuccess { success ->
                                updateStateCallback(
                                    "",
                                    success.toIndentString(includeSpace = true),
                                    false
                                )
                                Timber.d("Authenticate success = $success")
                                onAuthenticateEvent(
                                    AuthenticateEvent("Authenticate success!\nYou can start exploring the Embedded SDK")
                                )
                                onAuthenticateSuccess?.invoke(success)
                            }
                            result.onFailure { failure ->
                                updateStateCallback(
                                    state.authenticateUrl,
                                    failure.toIndentString(),
                                    false
                                )
                                Timber.e("Authenticate failure = $failure")
                                onAuthenticateEvent(AuthenticateEvent("Authenticate failed"))
                                onAuthenticateFailure?.invoke(failure)
                            }
                        }
                        .catch { error ->
                            updateStateCallback(
                                state.authenticateUrl,
                                error.toIndentString(),
                                false
                            )
                            Timber.e("Authenticate exception = ${error.message}")
                            onAuthenticateEvent(AuthenticateEvent("Authenticate failed"))
                            onAuthenticateError?.invoke(error)
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

    private fun onAuthenticateEvent(event: EmbeddedAuthenticateEvents) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    fun onGetAuthenticationContext() {
        state = state.copy(buttonPressed = "Get Authentication context")

        setCodeChallengeAndVerifier()

        val updateStateCallback = object : UpdateStateCallback {
            override fun invoke(url: String, result: String, progress: Boolean) {
                state = state.copy(
                    getAuthenticationContextResult = result,
                    getAuthenticationContextProgress = progress
                )
            }
        }

        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            try {
                ResponseUtil.onResponse(
                    method = "GetAuthenticationContext",
                    response = BeyondIdentityRetrofitBuilder.BEYOND_IDENTITY_API_SERVICE.authorize(
                        codeChallenge = state.codeChallenge,
                        redirectUri = BeyondIdentityConfig.REDIRECT_URI
                    ),
                    onSuccessResponse = { authorizeResponse ->
                        when (authorizeResponse?.authenticateUrl) {
                            null -> {
                                updateStateCallback(
                                    "",
                                    authorizeResponse.toIndentString(includeSpace = true),
                                    false
                                )
                            }
                            else -> {
                                updateStateCallback(
                                    "",
                                    "",
                                    true
                                )

                                EmbeddedSdk.getAuthenticationContext(
                                    url = authorizeResponse.authenticateUrl
                                )
                                    .flowOn(Dispatchers.Main + coroutineExceptionHandler)
                                    .onEach { result ->
                                        result.onSuccess { success ->
                                            Timber.d("Get Authentication Context success = $success")

                                            updateStateCallback(
                                                "",
                                                success.toIndentString(includeSpace = true),
                                                false
                                            )
                                        }
                                        result.onFailure { failure ->
                                            Timber.e("Get Authentication Context failure = $failure")

                                            updateStateCallback(
                                                "",
                                                failure.toIndentString(),
                                                false
                                            )
                                        }
                                    }
                                    .catch { error ->
                                        Timber.e("Get Authentication Context exception = ${error.message}")

                                        updateStateCallback(
                                            "",
                                            error.toIndentString(),
                                            false
                                        )
                                    }
                                    .launchIn(viewModelScope)
                            }
                        }
                    },
                    onFailureResponse = { authorizeResponse ->
                        updateStateCallback(
                            "",
                            authorizeResponse?.string().toIndentString(includeSpace = true),
                            false
                        )
                    }
                )
            } catch (e: Exception) {
                updateStateCallback(
                    "",
                    e.localizedMessage.toIndentString(includeSpace = true),
                    false
                )
            }
        }
    }

    fun onAuthenticateBeyondIdentity(activity: FragmentActivity) {
        state = state.copy(buttonPressed = "Authenticate with Beyond Identity")

        setCodeChallengeAndVerifier()

        val updateStateCallback = object : UpdateStateCallback {
            override fun invoke(url: String, result: String, progress: Boolean) {
                state = state.copy(
                    authenticateBeyondIdentityResult = result,
                    authenticateBeyondIdentityProgress = progress
                )
            }
        }

        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            try {
                ResponseUtil.onResponse(
                    method = "BeyondIdentityAuthorize",
                    response = BeyondIdentityRetrofitBuilder.BEYOND_IDENTITY_API_SERVICE.authorize(
                        codeChallenge = state.codeChallenge,
                        redirectUri = BeyondIdentityConfig.REDIRECT_URI
                    ),
                    onSuccessResponse = { authorizeResponse ->
                        when (authorizeResponse?.authenticateUrl) {
                            null -> {
                                updateStateCallback(
                                    "",
                                    authorizeResponse.toIndentString(includeSpace = true),
                                    false
                                )
                            }
                            else -> {
                                onAuthenticate(
                                    activity = activity,
                                    url = authorizeResponse.authenticateUrl,
                                    onAuthenticateSuccess = { success ->
                                        val redirectUri = success.redirectUrl.toUri()

                                        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
                                            try {
                                                ResponseUtil.onResponse(
                                                    method = "BeyondIdentityToken",
                                                    response = BeyondIdentityRetrofitBuilder.BEYOND_IDENTITY_API_SERVICE.token(
                                                        code = redirectUri?.getQueryParameter("code") ?: "",
                                                        codeVerifier = state.codeVerifier,
                                                        redirectUri = BeyondIdentityConfig.REDIRECT_URI
                                                    ),
                                                    onSuccessResponse = { tokenResponse ->
                                                        updateStateCallback(
                                                            "",
                                                            tokenResponse.toIndentString(includeSpace = true),
                                                            false
                                                        )
                                                    },
                                                    onFailureResponse = { tokenResponse ->
                                                        updateStateCallback(
                                                            "",
                                                            tokenResponse?.string().toIndentString(includeSpace = true),
                                                            false
                                                        )
                                                    }
                                                )
                                            } catch (e: Exception) {
                                                updateStateCallback(
                                                    "",
                                                    e.localizedMessage.toIndentString(includeSpace = true),
                                                    false
                                                )
                                            }
                                        }
                                    },
                                    updateStateCallback = updateStateCallback
                                )
                            }
                        }
                    },
                    onFailureResponse = { authorizeResponse ->
                        updateStateCallback(
                            "",
                            authorizeResponse?.string().toIndentString(includeSpace = true),
                            false
                        )
                    }
                )
            } catch (e: Exception) {
                updateStateCallback(
                    "",
                    e.localizedMessage.toIndentString(includeSpace = true),
                    false
                )
            }
        }
    }

    fun onAuthenticateOktaSdk(activity: FragmentActivity) {
        state = state.copy(buttonPressed = "Authenticate with Okta (SDK)")

        val customConfiguration = CustomConfiguration.Builder()
            .authorizationEndpoint(OktaConfig.AUTHORIZATION_ENDPOINT)
            .tokenEndpoint(OktaConfig.TOKEN_ENDPOINT)
            .create()

        val oidcConfig = OIDCConfig.Builder()
            .clientId(OktaConfig.CLIENT_ID)
            .redirectUri(OktaConfig.REDIRECT_URI)
            .endSessionRedirectUri(OktaConfig.END_SESSION_REDIRECT_URI)
            .scopes(OktaConfig.SCOPES)
            .discoveryUri(OktaConfig.DISCOVERY_URI)
            .customConfiguration(customConfiguration)
            .create()

        val webAuthClient = WebAuthBuilder()
            .setRequireHardwareBackedKeyStore(false)
            .withConfig(oidcConfig)
            .withContext(activity.applicationContext)
            .withStorage(SharedPreferenceStorage(activity))
            .create()

        val sessionClient = webAuthClient?.sessionClient

        webAuthClient?.registerCallback(
            object :
                ResultCallback<AuthorizationStatus?, AuthorizationException?> {
                override fun onSuccess(status: AuthorizationStatus) {
                    Timber.d("onSuccess($status)")
                    when (status) {
                        AuthorizationStatus.AUTHORIZED -> {
                            // Authorized authorization status. User is authorized. Received access, refresh, and ID tokens.
                            sessionClient?.tokens?.let { tokens ->
                                state = state.copy(
                                    authenticateOktaSDKResult = "{\"status\":$status,\"tokens\":{\"idToken\":${tokens.idToken},\"accessToken\":${tokens.accessToken},\"refreshToken\":${tokens.refreshToken},\"expiresIn\":${tokens.expiresIn},\"scope\":${tokens.scope},\"isAccessTokenExpired\":${tokens.isAccessTokenExpired}}}".toIndentString(
                                        includeSpace = true
                                    ),
                                    authenticateOktaSDKProgress = false
                                )
                            }
                        }
                        AuthorizationStatus.SIGNED_OUT -> {
                            // Signed out authorization status. Browser session is cleared.
                            state = state.copy(
                                authenticateOktaSDKResult = "{\"status\":$status}".toIndentString(
                                    includeSpace = true
                                ),
                                authenticateOktaSDKProgress = false
                            )
                        }
                        AuthorizationStatus.CANCELED -> {
                            // Operation was canceled.
                            state = state.copy(
                                authenticateOktaSDKResult = "{\"status\":$status}".toIndentString(
                                    includeSpace = true
                                ),
                                authenticateOktaSDKProgress = false
                            )
                        }
                        AuthorizationStatus.ERROR -> {
                            // Operation resulted in an exception.
                            state = state.copy(
                                authenticateOktaSDKResult = "{\"status\":$status}".toIndentString(
                                    includeSpace = true
                                ),
                                authenticateOktaSDKProgress = false
                            )
                        }
                        AuthorizationStatus.EMAIL_VERIFICATION_AUTHENTICATED -> {
                            // Email verified and user is authenticated with a valid browser session but the user is not
                            // authorized so it won't have any valid tokens. To complete the code exchange, client's
                            // should call {@link com.okta.oidc.clients.web.WebAuthClient#signIn(Activity, AuthenticationPayload)}
                            // again. Since the user already have a valid browser session(AUTHENTICATED),
                            // they are not required to enter any credentials.
                            state = state.copy(
                                authenticateOktaSDKResult = "{\"status\":$status}".toIndentString(
                                    includeSpace = true
                                ),
                                authenticateOktaSDKProgress = false
                            )
                        }
                        AuthorizationStatus.EMAIL_VERIFICATION_UNAUTHENTICATED -> {
                            // Email verified but user is not authenticated. To complete the code exchange, client's
                            // should call
                            // {@link com.okta.oidc.clients.web.WebAuthClient#signIn(Activity, AuthenticationPayload)}
                            // again. Since the user is not authenticated they are required to enter credentials.
                            // It is good practice to set the login hint in the payload so users don't have to enter it
                            // again. This is done automatically if using
                            // {@link com.okta.oidc.clients.web.WebAuthClient}
                            state = state.copy(
                                authenticateOktaSDKResult = "{\"status\":$status}".toIndentString(
                                    includeSpace = true
                                ),
                                authenticateOktaSDKProgress = false
                            )
                        }
                    }
                }

                override fun onCancel() {
                    Timber.d("onCancel()")
                    state = state.copy(
                        authenticateOktaSDKResult = "{\"status\":onCancel}".toIndentString(
                            includeSpace = true
                        ),
                        authenticateOktaSDKProgress = false
                    )
                }

                override fun onError(msg: String?, exception: AuthorizationException?) {
                    Timber.d("onError($msg, $exception)")
                    state = state.copy(
                        authenticateOktaSDKResult = "{\"status\":onError,\"msg\":$msg,\"exception\":$exception}".toIndentString(
                            includeSpace = true
                        ),
                        authenticateOktaSDKProgress = false
                    )
                }
            },
            activity
        )

        setCodeChallengeAndVerifier()

        val authenticationPayload = AuthenticationPayload.Builder()
            .addParameter("code_challenge", state.codeChallenge)
            .addParameter("code_challenge_method", "S256")
            .addParameter("code_verifier", state.codeVerifier)
            .setIdp(OktaConfig.IDP_ID)
            .build()

        webAuthClient?.signIn(activity, authenticationPayload)
    }

    fun onAuthenticateOktaWeb(activity: FragmentActivity) {
        state = state.copy(buttonPressed = "Authenticate with Okta (Web)")

        setCodeChallengeAndVerifier()

        startActivityForWebMode(
            activity,
            OktaConfig.getPkceAuthorizeUrl(
                code_challenge = state.codeChallenge,
                redirect_uri = OktaConfig.WEB_REDIRECT_URI
            ),
            IntentConstants.OKTA_WEB_RC
        )
    }

    fun onOktaTokenEndpoint(
        code: String,
        codeVerifier: String,
        redirectUri: String,
        onSuccess: OktaTokenSuccessCallback? = null,
        onFailure: OktaTokenFailureCallback? = null,
        updateStateCallback: UpdateStateCallback
    ) {
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            ResponseUtil.onResponse(
                method = "onOktaTokenEndpoint",
                response = OktaRetrofitBuilder.OKTA_API_SERVICE.v1Token(
                    code = code,
                    codeVerifier = codeVerifier,
                    redirectUri = redirectUri
                ),
                onSuccessResponse = { success ->
                    updateStateCallback(
                        "",
                        success.toIndentString(includeSpace = true),
                        false
                    )
                    onAuthenticateEvent(
                        AuthenticateEvent("Okta Authenticate success!\nYou can start exploring the Embedded SDK")
                    )
                    onSuccess?.invoke(success)
                },
                onFailureResponse = { failure ->
                    updateStateCallback(
                        "",
                        failure?.string().toIndentString(includeSpace = true),
                        false
                    )
                    onAuthenticateEvent(AuthenticateEvent("Okta Authenticate failure!"))
                    onFailure?.invoke(failure)
                }
            )
        }
    }

    fun onAuthenticateAuth0Sdk(activity: FragmentActivity) {
        state = state.copy(buttonPressed = "Authenticate with Auth0 (SDK)")

        setCodeChallengeAndVerifier()

        startActivityForWebMode(
            activity,
            Auth0Config.getPkceAuthorizeUrl(
                code_challenge = state.codeChallenge,
                redirect_uri = Auth0Config.WEB_REDIRECT_URI
            ),
            IntentConstants.AUTH0_SDK_RC
        )
    }

    fun onAuthenticateAuth0Web(activity: FragmentActivity) {
        state = state.copy(buttonPressed = "Authenticate with Auth0 (Web)")

        setCodeChallengeAndVerifier()

        startActivityForWebMode(
            activity,
            Auth0Config.getPkceAuthorizeUrl(
                code_challenge = state.codeChallenge,
                redirect_uri = Auth0Config.WEB_REDIRECT_URI
            ),
            IntentConstants.AUTH0_WEB_RC
        )
    }

    fun onAuth0TokenEndpoint(
        code: String,
        codeVerifier: String,
        redirectUri: String,
        onSuccess: Auth0TokenSuccessCallback? = null,
        onFailure: Auth0TokenFailureCallback? = null,
        updateStateCallback: UpdateStateCallback
    ) {
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            ResponseUtil.onResponse(
                method = "onAuth0TokenEndpoint",
                response = Auth0RetrofitBuilder.AUTH0_API_SERVICE.token(
                    code = code,
                    codeVerifier = codeVerifier,
                    redirectUri = redirectUri
                ),
                onSuccessResponse = { success ->
                    updateStateCallback(
                        "",
                        success.toIndentString(includeSpace = true),
                        false
                    )
                    onAuthenticateEvent(
                        AuthenticateEvent("Auth0 Authenticate success!\nYou can start exploring the Embedded SDK")
                    )
                    onSuccess?.invoke(success)
                },
                onFailureResponse = { failure ->
                    updateStateCallback(
                        "",
                        failure?.string().toIndentString(includeSpace = true),
                        false
                    )
                    onAuthenticateEvent(AuthenticateEvent("Auth0 Authenticate failure!"))
                    onFailure?.invoke(failure)
                }
            )
        }
    }

    fun onAuthenticateEmailOtp(email: String) {
        state = state.copy(buttonPressed = "Authenticate Email OTP")

        setCodeChallengeAndVerifier()

        val updateStateCallback = object : UpdateStateCallback {
            override fun invoke(url: String, result: String, progress: Boolean) {
                state = state.copy(
                    authenticateEmailOtpResult = result,
                    authenticateEmailOtpProgress = progress
                )
            }
        }

        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            try {
                ResponseUtil.onResponse(
                    method = "AuthenticateEmailOtp",
                    response = BeyondIdentityRetrofitBuilder.BEYOND_IDENTITY_API_SERVICE.authorize(
                        codeChallenge = state.codeChallenge,
                        redirectUri = BeyondIdentityConfig.REDIRECT_URI
                    ),
                    onSuccessResponse = { authorizeResponse ->
                        when (authorizeResponse?.authenticateUrl) {
                            null -> {
                                updateStateCallback(
                                    "",
                                    authorizeResponse.toIndentString(includeSpace = true),
                                    false
                                )
                            }
                            else -> {
                                updateStateCallback(
                                    "",
                                    "",
                                    true
                                )

                                state = state.copy(emailOtpUrl = "")

                                EmbeddedSdk.authenticateOtp(
                                    url = authorizeResponse.authenticateUrl,
                                    email = email
                                )
                                    .flowOn(Dispatchers.Main + coroutineExceptionHandler)
                                    .onEach { result ->
                                        result.onSuccess { success ->
                                            Timber.d("Authenticate OTP success = $success")

                                            state = state.copy(emailOtpUrl = success.url)

                                            updateStateCallback(
                                                "",
                                                success.toIndentString(includeSpace = true),
                                                false
                                            )
                                        }
                                        result.onFailure { failure ->
                                            Timber.e("Authenticate OTP failure = $failure")

                                            state = state.copy(emailOtpUrl = "")

                                            updateStateCallback(
                                                "",
                                                failure.toIndentString(),
                                                false
                                            )
                                        }
                                    }
                                    .catch { error ->
                                        Timber.e("Authenticate OTP exception = ${error.message}")

                                        state = state.copy(emailOtpUrl = "")

                                        updateStateCallback(
                                            "",
                                            error.toIndentString(),
                                            false
                                        )
                                    }
                                    .launchIn(viewModelScope)
                            }
                        }
                    },
                    onFailureResponse = { authorizeResponse ->
                        updateStateCallback(
                            "",
                            authorizeResponse?.string().toIndentString(includeSpace = true),
                            false
                        )
                    }
                )
            } catch (e: Exception) {
                updateStateCallback(
                    "",
                    e.localizedMessage.toIndentString(includeSpace = true),
                    false
                )
            }
        }
    }

    fun onRedeemEmailOtp(otp: String) {
        state = state.copy(buttonPressed = "Redeem Email OTP")

        setCodeChallengeAndVerifier()

        val updateStateCallback = object : UpdateStateCallback {
            override fun invoke(url: String, result: String, progress: Boolean) {
                state = state.copy(
                    redeemEmailOtpResult = result,
                    redeemEmailOtpProgress = progress
                )
            }
        }

        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            try {
                EmbeddedSdk.redeemOtp(
                    url = state.emailOtpUrl,
                    otp = otp
                )
                    .flowOn(Dispatchers.Main + coroutineExceptionHandler)
                    .onEach { result ->
                        result.onSuccess { success ->
                            Timber.d("Redeem OTP success = $success")

                            updateStateCallback(
                                "",
                                success.toIndentString(includeSpace = true),
                                false
                            )
                        }
                        result.onFailure { failure ->
                            Timber.e("Redeem OTP failure = $failure")

                            updateStateCallback(
                                "",
                                failure.toIndentString(),
                                false
                            )
                        }
                    }
                    .catch { error ->
                        Timber.e("Redeem OTP exception = ${error.message}")

                        updateStateCallback(
                            "",
                            error.toIndentString(),
                            false
                        )
                    }
                    .launchIn(viewModelScope)
            } catch (e: Exception) {
                updateStateCallback(
                    "",
                    e.localizedMessage.toIndentString(includeSpace = true),
                    false
                )
            }
        }
    }

    private fun onButtonPressedActionHandler(
        onBeyondIdentity: Callback? = null,
        onAuth0SDK: Callback? = null,
        onAuth0Web: Callback? = null,
        onOktaSDK: Callback? = null,
        onOktaWeb: Callback? = null,
        onException: Callback? = null
    ) {
        when {
            state.buttonPressed.contains("Beyond Identity", ignoreCase = true) -> {
                onBeyondIdentity?.invoke()
            }
            state.buttonPressed.contains("Auth0", ignoreCase = true) -> {
                when {
                    state.buttonPressed.contains("SDK", ignoreCase = true) -> {
                        onAuth0SDK?.invoke()
                    }
                    state.buttonPressed.contains("Web", ignoreCase = true) -> {
                        onAuth0Web?.invoke()
                    }
                    else -> {
                        onException?.invoke()
                    }
                }
            }
            state.buttonPressed.contains("Okta", ignoreCase = true) -> {
                when {
                    state.buttonPressed.contains("SDK", ignoreCase = true) -> {
                        onOktaSDK?.invoke()
                    }
                    state.buttonPressed.contains("Web", ignoreCase = true) -> {
                        onOktaWeb?.invoke()
                    }
                    else -> {
                        onException?.invoke()
                    }
                }
            }
            else -> {
                onException?.invoke()
            }
        }
    }

    private fun onButtonPressedRequestCode(): Int = when {
        state.buttonPressed.contains("Beyond Identity", ignoreCase = true) -> {
            IntentConstants.BEYOND_IDENTITY_RC
        }
        state.buttonPressed.contains("Auth0", ignoreCase = true) -> {
            when {
                state.buttonPressed.contains("SDK", ignoreCase = true) -> {
                    IntentConstants.AUTH0_SDK_RC
                }
                state.buttonPressed.contains("Web", ignoreCase = true) -> {
                    IntentConstants.AUTH0_WEB_RC
                }
                else -> {
                    throw Throwable("Invalid Button Pressed")
                }
            }
        }
        state.buttonPressed.contains("Okta", ignoreCase = true) -> {
            when {
                state.buttonPressed.contains("SDK", ignoreCase = true) -> {
                    IntentConstants.OKTA_SDK_RC
                }
                state.buttonPressed.contains("Web", ignoreCase = true) -> {
                    IntentConstants.OKTA_WEB_RC
                }
                else -> {
                    throw Throwable("Invalid Button Pressed")
                }
            }
        }
        else -> {
            throw Throwable("Invalid Button Pressed")
        }
    }

    private fun setCodeChallengeAndVerifier() {
        val codeVerifier = PKCEUtil.generateCodeVerifier()
        val codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier)
        state = state.copy(codeVerifier = codeVerifier, codeChallenge = codeChallenge)
    }
}
