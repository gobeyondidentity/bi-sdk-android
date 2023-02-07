package com.beyondidentity.authenticator.sdk.android.embedded.urlvalidation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beyondidentity.authenticator.sdk.android.embedded.utils.resetResult
import com.beyondidentity.authenticator.sdk.android.utils.toIndentString
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class EmbeddedUrlValidationViewModel : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e("Caught $exception")
    }

    var state: EmbeddedUrlValidationState by mutableStateOf(EmbeddedUrlValidationState())
        private set

    fun onUrlValidationBindPasskeyUrlTextChange(text: String) {
        state = state.copy(urlValidationBindPasskeyUrl = text)
    }

    fun onUrlValidationAuthenticateUrlTextChange(text: String) {
        state = state.copy(urlValidationAuthenticateUrl = text)
    }

    fun onValidateBindPasskeyUrl() {
        if (!resetResult(
                state.urlValidationBindPasskeyUrl,
                "Please provide a Bind Passkey URL",
            ) { _, result, progress ->
                state = state.copy(
                    validateBindPasskeyUrlResult = result,
                    validateBindPasskeyUrlProgress = progress,
                )
            }
        ) {
            return
        }

        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            val result = EmbeddedSdk.isBindPasskeyUrl(state.urlValidationBindPasskeyUrl)

            state = state.copy(
                validateBindPasskeyUrlResult = result.toIndentString(),
                validateBindPasskeyUrlProgress = false,
                urlValidationBindPasskeyUrl = "",
            )
            Timber.d("got result for validateBindPasskeyUrl = $result")
        }
    }

    fun onValidateAuthenticateUrl() {
        if (!resetResult(
                state.urlValidationAuthenticateUrl,
                "Please provide an Authenticate URL",
            ) { _, result, progress ->
                state = state.copy(
                    validateAuthenticateUrlResult = result,
                    validateAuthenticateUrlProgress = progress,
                )
            }
        ) {
            return
        }

        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            val result = EmbeddedSdk.isAuthenticateUrl(state.urlValidationAuthenticateUrl)

            state = state.copy(
                validateAuthenticateUrlResult = result.toIndentString(),
                validateAuthenticateUrlProgress = false,
                urlValidationAuthenticateUrl = "",
            )
            Timber.d("got result for validateAuthenticateUrl = $result")
        }
    }
}
