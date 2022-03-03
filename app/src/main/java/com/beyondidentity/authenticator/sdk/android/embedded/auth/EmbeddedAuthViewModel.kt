package com.beyondidentity.authenticator.sdk.android.embedded.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beyondidentity.authenticator.sdk.android.BuildConfig
import com.beyondidentity.authenticator.sdk.android.utils.RetrofitBuilder
import com.beyondidentity.authenticator.sdk.android.utils.toIndentString
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class EmbeddedAuthViewModel : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e("Caught $exception")
    }

    var state: EmbeddedAuthState by mutableStateOf(EmbeddedAuthState())
        private set

    fun onGeneratePkce() {
        EmbeddedSdk.createPkce()
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onEach { result ->
                result.onSuccess { pkce ->
                    Timber.d("got result for pkce = ${pkce.toIndentString()}")
                    state = state.copy(pkce = pkce, pkceResult = pkce.toIndentString())
                }
                result.onFailure { t ->
                    Timber.e("error getting PKCE $t")
                    state = state.copy(pkce = null, pkceResult = t.toString())
                }
            }
            .catch {
                val message = "PKCE generation failed ${it.message}"
                Timber.d(message)
                state = state.copy(pkce = null, pkceResult = message)
            }
            .launchIn(viewModelScope)
    }

    fun onAuthenticate() {
        EmbeddedSdk.authenticate(
            clientId = BuildConfig.BUILD_CONFIG_BI_DEMO_PUBLIC_CLIENT_ID,
            redirectUri = "${BuildConfig.BUILD_CONFIG_BEYOND_IDENTITY_SDK_SAMPLEAPP_SCHEME}://",
        )
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onEach { result ->
                result.onSuccess { tokenResponse ->
                    Timber.d("Authentication result = ${tokenResponse.toIndentString()}")
                    state = state.copy(authenticationResult = tokenResponse.toIndentString())
                }
                result.onFailure { t ->
                    Timber.e("error confidential auth $t")
                    state = state.copy(authenticationResult = "Failed to authenticate = $t")
                }
            }
            .catch {
                val message = "Failed to authenticate ${it.message}"
                Timber.d(message)
                state = state.copy(authenticationResult = message)
            }
            .launchIn(viewModelScope)
    }

    fun onAuthorize() {
        state = state.copy(authorizeExchangeResult = "")
        EmbeddedSdk.authorize(
            clientId = BuildConfig.BUILD_CONFIG_BI_DEMO_CONFIDENTIAL_CLIENT_ID,
            redirectUri = "${BuildConfig.BUILD_CONFIG_BEYOND_IDENTITY_SDK_SAMPLEAPP_SCHEME}://",
            scope = "openid",
            pkceS256CodeChallenge = state.pkce?.codeChallenge,
        )
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onEach { result ->
                result.onSuccess { code ->
                    Timber.d("got result for auth confidential client = $code")
                    state = state.copy(authorizeCode = code, authorizeResult = "Authorization code = $code")
                }
                result.onFailure { t ->
                    Timber.e("error confidential auth $t")
                    state = state.copy(authorizeCode = "", authorizeResult = "Failed getting authorization code $t")
                }
            }

            .catch {
                val message = "Failed getting authorization code ${it.message}"
                Timber.d(message)
                state = state.copy(authorizeCode = "", authorizeResult = message)
            }
            .launchIn(viewModelScope)
    }

    fun onAuthorizeExchange() {
        if (state.authorizeCode.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
                val token = RetrofitBuilder.BI_API_SERVICE.getToken(
                    code = state.authorizeCode,
                    redirectUri = "${BuildConfig.BUILD_CONFIG_BEYOND_IDENTITY_SDK_SAMPLEAPP_SCHEME}://",
                    grantType = "authorization_code",
                    code_verifier = state.pkce?.codeVerifier,
                )

                state = state.copy(
                    pkceResult = "",
                    pkce = null,
                    authorizeCode = "",
                    authorizeResult = "",
                    authorizeExchangeResult = token.toIndentString()
                )
                Timber.d(token.toIndentString())
            }
        } else {
            state = state.copy(authorizeExchangeResult = "Get authorization code first.")
        }
    }
}