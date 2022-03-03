package com.beyondidentity.authenticator.sdk.android.embedded.regandrecover

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beyondidentity.authenticator.sdk.android.utils.CreateUserRequest
import com.beyondidentity.authenticator.sdk.android.utils.RecoverUserRequest
import com.beyondidentity.authenticator.sdk.android.utils.RetrofitBuilder
import java.util.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class RegisterAndRecoverViewModel : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e("Caught $exception")
    }

    var state: RegAndRecoverState by mutableStateOf(RegAndRecoverState())
        private set

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

            state = state.copy(registerResult = result.toString(), registerEmail = "")
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

            state = state.copy(recoverResult = result.toString(), recoverEmail = "")
            Timber.d("got result for recoverUser = $result")
        }
    }
}
