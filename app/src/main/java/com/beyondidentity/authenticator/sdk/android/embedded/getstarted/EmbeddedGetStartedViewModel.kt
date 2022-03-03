package com.beyondidentity.authenticator.sdk.android.embedded.getstarted

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.CredentialRegistration
import com.beyondidentity.authenticator.sdk.android.utils.CreateUserRequest
import com.beyondidentity.authenticator.sdk.android.utils.RecoverUserRequest
import com.beyondidentity.authenticator.sdk.android.utils.RetrofitBuilder
import com.beyondidentity.authenticator.sdk.android.utils.toIndentString
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import java.util.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class EmbeddedGetStartedViewModel : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e("Caught $exception")
    }

    var state: EmbeddedGetStartedState by mutableStateOf(EmbeddedGetStartedState())
        private set

    private val _events = MutableSharedFlow<EmbeddedGetStartedEvents>()
    val events: SharedFlow<EmbeddedGetStartedEvents> = _events

    fun onRegistrationEmailTextChange(text: String) {
        state = state.copy(registerEmail = text)
    }

    fun onRecoverEmailTextChange(text: String) {
        state = state.copy(recoverEmail = text)
    }

    fun onRegisterUser() {
        state = state.copy(registerResult = "")
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            val result = RetrofitBuilder.ACME_API_SERVICE.createUser(
                CreateUserRequest(
                    bindingTokenDeliveryMethod = "email",
                    externalId = state.registerEmail,
                    email = state.registerEmail,
                    displayName = UUID.randomUUID().toString(),
                    userName = UUID.randomUUID().toString(),
                )
            )

            state = state.copy(registerResult = result.toIndentString(), registerEmail = "")
            Timber.d("got result for createUser = $result")
        }
    }

    fun onRecoverUser() {
        state = state.copy(recoverResult = "")
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            val result = RetrofitBuilder.ACME_API_SERVICE.recoverUser(
                RecoverUserRequest(
                    bindingTokenDeliveryMethod = "email",
                    externalId = state.recoverEmail,
                )
            )

            state = state.copy(recoverResult = result.toIndentString(), recoverEmail = "")
            Timber.d("got result for recoverUser = $result")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun registerCredentialWithUrl(url: String) {
        EmbeddedSdk.registerCredentialsWithUrl(url)
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onEach {
                it.onSuccess { cred ->
                    Timber.d("Credential registered = $cred")
                    onGetStartedEvent(CredentialRegistration("Credential successfully registered!\nYou can start exploring the Embedded SDK"))
                }
                it.onFailure { t ->
                    Timber.e("Credential registration failed $t")
                    onGetStartedEvent(CredentialRegistration("Credential registration failed"))
                }
            }
            .catch {
                val message = "Cancel extend credential failed ${it.message}"
                Timber.e(message)
                onGetStartedEvent(CredentialRegistration("Credential registration failed"))
            }
            .launchIn(viewModelScope)
    }

    fun onGetStartedEvent(event: EmbeddedGetStartedEvents) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}