package com.beyondidentity.authenticator.sdk.android.embedded.extend

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beyondidentity.authenticator.sdk.android.BuildConfig
import com.beyondidentity.authenticator.sdk.android.utils.toIndentString
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class ExtendCredentialViewModel : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e("Caught $exception")
    }

    var state: ExtendCredentialState by mutableStateOf(ExtendCredentialState())
        private set

    fun onCredentialExtend() {
        state = state.copy(generatedExtendToken = "", cancelExtendResult = "")
        EmbeddedSdk.extendCredentials(
            credentialHandles = listOf(BuildConfig.BUILD_CONFIG_BEYOND_IDENTITY_DEMO_TENANT),
        )
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onCompletion {
                Timber.d("Extend completed")
                state = state.copy(generatedExtendToken = "Extend completed")
            }
            .onEach {
                Timber.d("Got extend token = $it")
                it?.let {
                    state = state.copy(generatedExtendToken = it.rendezvousToken)
                }
            }
            .catch {
                val message = "Extend credential failed = ${it.message}"
                Timber.d(message)
                state = state.copy(generatedExtendToken = if (it.message == "aborted" || it.message == "Cancelled") "Cancelled" else message)
            }
            .launchIn(viewModelScope)
    }

    fun onCancelExtendCredential() {
        EmbeddedSdk.cancelExtendCredentials()
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onEach {
                state = state.copy(cancelExtendResult = "Extend Credential Cancelled", generatedExtendToken = "Cancelled")
            }
            .catch {
                val message = "Cancel extend credential failed ${it.message}"
                Timber.d(message)
                state = state.copy(cancelExtendResult = message)
            }
            .launchIn(viewModelScope)
    }

    fun onRegisterCredentialWithToken() {
        EmbeddedSdk.registerCredentialsWithToken(state.registerTokenInputValue)
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onCompletion { Timber.d("Credential extend completed") }
            .onEach {
                Timber.d("Registered profiles $it")
                it.onSuccess {  credentials ->
                    state = state.copy(registerTokenInputValue = "", registerCredentialWithTokenResult = credentials.toIndentString())
                }
                it.onFailure { t ->
                    state = state.copy(registerTokenInputValue = "", registerCredentialWithTokenResult = t.toIndentString())
                }
            }
            .catch {
                val message = "Extend credential failed ${it.message}"
                Timber.d(message)
                state = state.copy(registerTokenInputValue = "", registerCredentialWithTokenResult = message)
            }
            .launchIn(viewModelScope)
    }

    fun onRegisterTokenInputValueChanged(token: String) {
        state = state.copy(registerTokenInputValue = token)
    }
}