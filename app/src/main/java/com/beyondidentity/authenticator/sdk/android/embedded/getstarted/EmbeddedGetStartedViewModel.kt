package com.beyondidentity.authenticator.sdk.android.embedded.getstarted

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.BindCredentialEvent
import com.beyondidentity.authenticator.sdk.android.embedded.utils.BindCredentialErrorCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.BindCredentialFailureCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.BindCredentialSuccessCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.UpdateStateCallback
import com.beyondidentity.authenticator.sdk.android.utils.CredentialBindingLinkRequest
import com.beyondidentity.authenticator.sdk.android.utils.CredentialBindingResponse
import com.beyondidentity.authenticator.sdk.android.utils.RecoverCredentialBindingLinkRequest
import com.beyondidentity.authenticator.sdk.android.utils.RetrofitBuilder
import com.beyondidentity.authenticator.sdk.android.utils.toIndentString
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber

class EmbeddedGetStartedViewModel : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e("Caught $exception")
    }

    var state: EmbeddedGetStartedState by mutableStateOf(EmbeddedGetStartedState())
        private set

    private val _events = MutableSharedFlow<EmbeddedGetStartedEvents>()
    val events: SharedFlow<EmbeddedGetStartedEvents> = _events

    fun onCredentialBindingLinkUsernameTextChange(text: String) {
        state = state.copy(registerUsername = text)
    }

    fun onRecoverCredentialBindingLinkUsernameTextChange(text: String) {
        state = state.copy(recoverUsername = text)
    }

    fun onBindCredentialUrlTextChange(text: String) {
        state = state.copy(bindCredentialUrl = text)
    }

    fun onRegisterCredential(username: String) {
        state = state.copy(registerResult = "")
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            val result = RetrofitBuilder.ACME_API_SERVICE.credentialBindingLink(
                CredentialBindingLinkRequest(username = username),
            )

            onCredentialBindingResponse(
                "onRegisterCredential",
                result,
                object : UpdateStateCallback {
                    override fun invoke(username: String, result: String) {
                        state = state.copy(
                            registerUsername = username,
                            registerResult = result,
                        )
                    }
                },
            )
        }
    }

    fun onRecoverCredential(username: String) {
        state = state.copy(recoverResult = "")
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            val result = RetrofitBuilder.ACME_API_SERVICE.recoverCredentialBindingLink(
                RecoverCredentialBindingLinkRequest(username = username),
            )

            onCredentialBindingResponse(
                "onRecoverCredential",
                result,
                object : UpdateStateCallback {
                    override fun invoke(username: String, result: String) {
                        state = state.copy(
                            recoverUsername = username,
                            recoverResult = result,
                        )
                    }
                },
            )
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private fun onCredentialBindingResponse(
        method: String,
        response: Response<CredentialBindingResponse>,
        updateStateCallback: UpdateStateCallback,
    ) {
        if (response.isSuccessful) {
            when (response.body()?.credentialBindingLink) {
                null -> {
                    updateStateCallback(
                        "",
                        response.body().toIndentString(includeSpace = true),
                    )
                }
                else -> {
                    onBindCredential(
                        url = response.body()?.credentialBindingLink!!,
                        updateStateCallback = updateStateCallback,
                    )
                }
            }
        } else {
            updateStateCallback(
                "",
                response.errorBody()?.string().toIndentString(includeSpace = true),
            )
        }

        Timber.d(
            "got result for $method = ${
                response.body() ?: response.errorBody()?.string()
            }",
        )
    }

    fun onBindCredential(
        url: String,
        onBindCredentialSuccess: BindCredentialSuccessCallback? = null,
        onBindCredentialFailure: BindCredentialFailureCallback? = null,
        onBindCredentialError: BindCredentialErrorCallback? = null,
        updateStateCallback: UpdateStateCallback = object : UpdateStateCallback {
            override fun invoke(url: String, result: String) {
                state = state.copy(
                    bindCredentialUrl = url,
                    bindCredentialResult = result,
                )
            }
        },
    ) {
        EmbeddedSdk.bindCredential(
            url = url,
        )
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onEach {
                it.onSuccess { success ->
                    updateStateCallback(
                        "",
                        success.toIndentString(),
                    )
                    Timber.d("Bind Credential success = $success")
                    onBindCredentialEvent(BindCredentialEvent("Bind Credential success!\nYou can start exploring the Embedded SDK"))
                    onBindCredentialSuccess?.invoke(success)
                }
                it.onFailure { failure ->
                    updateStateCallback(
                        state.registerUsername,
                        failure.toIndentString(),
                    )
                    Timber.e("Bind Credential failure = $failure")
                    onBindCredentialEvent(BindCredentialEvent("Bind Credential failed"))
                    onBindCredentialFailure?.invoke(failure)
                }
            }
            .catch { error ->
                updateStateCallback(
                    state.bindCredentialUrl,
                    error.toIndentString(),
                )
                Timber.e("Bind Credential exception = ${error.message}")
                onBindCredentialEvent(BindCredentialEvent("Bind Credential failed"))
                onBindCredentialError?.invoke(error)
            }
            .launchIn(viewModelScope)
    }

    private fun onBindCredentialEvent(event: EmbeddedGetStartedEvents) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    fun onGetStartedEvent(event: EmbeddedGetStartedEvents) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}
