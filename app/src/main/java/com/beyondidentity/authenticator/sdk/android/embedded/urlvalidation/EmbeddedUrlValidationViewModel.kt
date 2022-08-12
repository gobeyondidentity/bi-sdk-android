package com.beyondidentity.authenticator.sdk.android.embedded.urlvalidation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun onUrlValidationBindCredentialUrlTextChange(text: String) {
        state = state.copy(urlValidationBindCredentialUrl = text)
    }

    fun onUrlValidationAuthenticateUrlTextChange(text: String) {
        state = state.copy(urlValidationAuthenticateUrl = text)
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
}
