package com.beyondidentity.authenticator.sdk.android.embedded.getstarted

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beyondidentity.authenticator.sdk.android.apis.AcmeRetrofitBuilder
import com.beyondidentity.authenticator.sdk.android.apis.CredentialBindingLinkRequest
import com.beyondidentity.authenticator.sdk.android.apis.RecoverCredentialBindingLinkRequest
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.BindPasskeyEvent
import com.beyondidentity.authenticator.sdk.android.embedded.utils.BindPasskeyErrorCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.BindPasskeyFailureCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.BindPasskeySuccessCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.UpdateStateCallback
import com.beyondidentity.authenticator.sdk.android.embedded.utils.resetResult
import com.beyondidentity.authenticator.sdk.android.utils.ResponseUtil
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

    fun onPasskeyBindingLinkUsernameTextChange(text: String) {
        state = state.copy(registerUsername = text)
    }

    fun onRecoverPasskeyBindingLinkUsernameTextChange(text: String) {
        state = state.copy(recoverUsername = text)
    }

    fun onBindPasskeyUrlTextChange(text: String) {
        state = state.copy(bindPasskeyUrl = text)
    }

    fun onRegisterPasskey(username: String) {
        if (!resetResult(
                username,
                "Please enter a username",
            ) { _, result, progress ->
                state = state.copy(
                    registerResult = result,
                    registerProgress = progress,
                )
            }
        ) {
            return
        }

        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            try {
                val result = AcmeRetrofitBuilder.ACME_API_SERVICE.credentialBindingLink(
                    CredentialBindingLinkRequest(username = username),
                )

                onCredentialBindingLinkResponse(
                    "onRegisterPasskey",
                    result,
                    result.body()?.credentialBindingLink,
                    object : UpdateStateCallback {
                        override fun invoke(username: String, result: String, progress: Boolean) {
                            state = state.copy(
                                registerUsername = username,
                                registerResult = result,
                                registerProgress = progress,
                            )
                        }
                    },
                )
            } catch (e: Exception) {
                state = state.copy(
                    registerUsername = username,
                    registerResult = e.localizedMessage.toIndentString(includeSpace = true),
                    registerProgress = false,
                )
            }
        }
    }

    fun onRecoverPasskey(username: String) {
        if (!resetResult(
                username,
                "Please enter a username",
            ) { _, result, progress ->
                state = state.copy(
                    recoverResult = result,
                    recoverProgress = progress,
                )
            }
        ) {
            return
        }

        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            try {
                val result = AcmeRetrofitBuilder.ACME_API_SERVICE.recoverCredentialBindingLink(
                    RecoverCredentialBindingLinkRequest(username = username),
                )

                onCredentialBindingLinkResponse(
                    "onRecoverPasskey",
                    result,
                    result.body()?.credentialBindingLink,
                    object : UpdateStateCallback {
                        override fun invoke(username: String, result: String, progress: Boolean) {
                            state = state.copy(
                                recoverUsername = username,
                                recoverResult = result,
                                recoverProgress = progress,
                            )
                        }
                    },
                )
            } catch (e: Exception) {
                state = state.copy(
                    recoverUsername = username,
                    recoverResult = e.localizedMessage.toIndentString(includeSpace = true),
                    recoverProgress = false,
                )
            }
        }
    }

    private fun onCredentialBindingLinkResponse(
        method: String,
        response: Response<*>,
        credentialBindingLink: String?,
        updateStateCallback: UpdateStateCallback,
    ) {
        ResponseUtil.onResponse(
            method = method,
            response = response,
            onSuccessResponse = { success ->
                when (credentialBindingLink) {
                    null -> {
                        updateStateCallback(
                            "",
                            success.toIndentString(includeSpace = true),
                            false,
                        )
                    }
                    else -> {
                        onBindPasskey(
                            url = credentialBindingLink,
                            updateStateCallback = updateStateCallback,
                        )
                    }
                }
            },
            onFailureResponse = { failure ->
                updateStateCallback(
                    "",
                    failure?.string().toIndentString(includeSpace = true),
                    false,
                )
            },
        )
    }

    fun onBindPasskey(
        url: String,
        onBindPasskeySuccess: BindPasskeySuccessCallback? = null,
        onBindPasskeyFailure: BindPasskeyFailureCallback? = null,
        onBindPasskeyError: BindPasskeyErrorCallback? = null,
        updateStateCallback: UpdateStateCallback = object : UpdateStateCallback {
            override fun invoke(url: String, result: String, progress: Boolean) {
                state = state.copy(
                    bindPasskeyUrl = url,
                    bindPasskeyResult = result,
                    bindPasskeyProgress = progress,
                )
            }
        },
    ) {
        if (!resetResult(
                string = url,
                result = "Please provide a Bind Passkey URL",
                updateStateCallback = updateStateCallback::invoke,
            )
        ) {
            return
        }

        EmbeddedSdk.bindPasskey(
            url = url,
        )
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onEach { result ->
                result.onSuccess { success ->
                    updateStateCallback(
                        "",
                        success.toIndentString(),
                        false,
                    )
                    Timber.d("Bind Passkey success = $success")
                    onBindPasskeyEvent(BindPasskeyEvent("Bind Passkey success!\nYou can start exploring the Embedded SDK"))
                    onBindPasskeySuccess?.invoke(success)
                }
                result.onFailure { failure ->
                    updateStateCallback(
                        state.registerUsername,
                        failure.toIndentString(),
                        false,
                    )
                    Timber.e("Bind Passkey failure = $failure")
                    onBindPasskeyEvent(BindPasskeyEvent("Bind Passkey failed"))
                    onBindPasskeyFailure?.invoke(failure)
                }
            }
            .catch { error ->
                updateStateCallback(
                    state.bindPasskeyUrl,
                    error.toIndentString(),
                    false,
                )
                Timber.e("Bind Passkey exception = ${error.message}")
                onBindPasskeyEvent(BindPasskeyEvent("Bind Passkey failed"))
                onBindPasskeyError?.invoke(error)
            }
            .launchIn(viewModelScope)
    }

    private fun onBindPasskeyEvent(event: EmbeddedGetStartedEvents) {
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
