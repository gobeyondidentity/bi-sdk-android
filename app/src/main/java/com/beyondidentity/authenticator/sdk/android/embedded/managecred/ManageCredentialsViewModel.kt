package com.beyondidentity.authenticator.sdk.android.embedded.managecred

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
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class ManageCredentialsViewModel : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e("Caught $exception")
    }

    var state: ManageCredentialState by mutableStateOf(ManageCredentialState())
        private set


    fun onGetCredentials() {
        EmbeddedSdk.getCredentials()
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onEach { result ->
                result.onSuccess { creds ->
                    Timber.d("Got credentials = ${creds.toIndentString()}")
                    state = state.copy(getCredentialResult = creds.toIndentString())
                }
                result.onFailure { t -> state = state.copy(getCredentialResult = t.toString()) }
            }
            .catch {
                val message = "Getting credentials failed ${it.message}"
                Timber.d(message)
                state = state.copy(getCredentialResult = message)
            }
            .launchIn(viewModelScope)
    }

    fun onDeleteCredentials() {
        EmbeddedSdk.deleteCredential(BuildConfig.BUILD_CONFIG_BEYOND_IDENTITY_DEMO_TENANT)
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onEach { result ->
                result.onSuccess {
                    Timber.d("Credentials deleted")
                    state = state.copy(deleteCredentialResult = "Credentials deleted", getCredentialResult = "[]")
                }
                result.onFailure {
                    Timber.d("Credentials deletion failed.")
                    state = state.copy(deleteCredentialResult = it.toString())
                }
            }
            .catch {
                val message = "Credentials deletion failed ${it.message}"
                Timber.d(message)
                state = state.copy(deleteCredentialResult = message)
            }
            .launchIn(viewModelScope)
    }
}