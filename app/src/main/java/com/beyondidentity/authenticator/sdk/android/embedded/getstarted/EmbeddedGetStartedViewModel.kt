package com.beyondidentity.authenticator.sdk.android.embedded.getstarted

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beyondidentity.authenticator.sdk.android.embedded.auth.SelectCredentialDialogFragment
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.AuthenticateEvent
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.BindCredentialEvent
import com.beyondidentity.authenticator.sdk.android.utils.toIndentString
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import com.beyondidentity.embedded.sdk.models.Credential
import com.beyondidentity.embedded.sdk.models.OnSelectCredential
import com.beyondidentity.embedded.sdk.models.OnSelectedCredential
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

class EmbeddedGetStartedViewModel : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e("Caught $exception")
    }

    var state: EmbeddedGetStartedState by mutableStateOf(EmbeddedGetStartedState())
        private set

    private val _events = MutableSharedFlow<EmbeddedGetStartedEvents>()
    val events: SharedFlow<EmbeddedGetStartedEvents> = _events

    fun onBindCredentialUrlTextChange(text: String) {
        state = state.copy(bindCredentialUrl = text)
    }

    fun onAuthenticateUrlTextChange(text: String) {
        state = state.copy(authenticateUrl = text)
    }

    fun onUrlValidationBindCredentialUrlTextChange(text: String) {
        state = state.copy(urlValidationBindCredentialUrl = text)
    }

    fun onUrlValidationAuthenticateUrlTextChange(text: String) {
        state = state.copy(urlValidationAuthenticateUrl = text)
    }

    fun onBindCredential(url: String) {
        EmbeddedSdk.bindCredential(url)
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onEach {
                it.onSuccess { success ->
                    state = state.copy(
                        bindCredentialUrl = "",
                        bindCredentialResult = success.toIndentString(),
                    )
                    Timber.d("Bind Credential success = $success")
                    onBindCredentialEvent(BindCredentialEvent("Bind Credential success!\nYou can start exploring the Embedded SDK"))
                }
                it.onFailure { failure ->
                    state = state.copy(
                        bindCredentialUrl = state.bindCredentialUrl,
                        bindCredentialResult = failure.toIndentString(),
                    )
                    Timber.e("Bind Credential failure = $failure")
                    onBindCredentialEvent(BindCredentialEvent("Bind Credential failed"))
                }
            }
            .catch {
                state = state.copy(
                    bindCredentialUrl = state.bindCredentialUrl,
                    bindCredentialResult = it.toIndentString(),
                )
                Timber.e("Bind Credential exception = ${it.message}")
                onBindCredentialEvent(BindCredentialEvent("Bind Credential failed"))
            }
            .launchIn(viewModelScope)
    }

    private fun onBindCredentialEvent(event: EmbeddedGetStartedEvents) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    private fun onSelectCredential(
        activity: FragmentActivity,
        credentials: List<Credential>,
        selectedCredentialCallback: OnSelectedCredential,
    ) {
        SelectCredentialDialogFragment.newInstance(credentials, selectedCredentialCallback)
            .show(activity.supportFragmentManager, SelectCredentialDialogFragment.TAG)
    }

    fun onAuthenticate(activity: FragmentActivity, url: String) {
        EmbeddedSdk.authenticate(
            url,
            object : OnSelectCredential {
                override fun invoke(
                    credentials: List<Credential>,
                    selectedCredentialCallback: OnSelectedCredential,
                ) {
                    onSelectCredential(activity, credentials, selectedCredentialCallback)
                }
            },
        )
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onEach {
                it.onSuccess { success ->
                    state = state.copy(
                        authenticateUrl = "",
                        authenticateResult = success.toIndentString(),
                    )
                    Timber.d("Authenticate success = $success")
                    onAuthenticateEvent(AuthenticateEvent("Authenticate success!\nYou can start exploring the Embedded SDK"))
                }
                it.onFailure { failure ->
                    state = state.copy(
                        authenticateUrl = state.authenticateUrl,
                        authenticateResult = failure.toIndentString(),
                    )
                    Timber.e("Authenticate failure = $failure")
                    onAuthenticateEvent(AuthenticateEvent("Authenticate failed"))
                }
            }
            .catch {
                state = state.copy(
                    authenticateUrl = state.authenticateUrl,
                    authenticateResult = it.toIndentString(),
                )
                Timber.e("Authenticate exception = ${it.message}")
                onAuthenticateEvent(AuthenticateEvent("Authenticate failed"))
            }
            .launchIn(viewModelScope)
    }

    private fun onAuthenticateEvent(event: EmbeddedGetStartedEvents) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    fun onValidateBindCredentialUrl() {
        state = state.copy(validateBindCredentialUrlResult = "")
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            val result = EmbeddedSdk.isBindCredentialUrl(state.urlValidationBindCredentialUrl)

            state = state.copy(
                validateBindCredentialUrlResult = result.toIndentString(),
                urlValidationBindCredentialUrl = "",
            )
            Timber.d("got result for validateBindCredentialUrl = $result")
        }
    }

    fun onValidateAuthenticateUrl() {
        state = state.copy(validateAuthenticateUrlResult = "")
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            val result = EmbeddedSdk.isAuthenticateUrl(state.urlValidationAuthenticateUrl)

            state = state.copy(
                validateAuthenticateUrlResult = result.toIndentString(),
                urlValidationAuthenticateUrl = "",
            )
            Timber.d("got result for validateAuthenticateUrl = $result")
        }
    }

    fun onGetStartedEvent(event: EmbeddedGetStartedEvents) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}
