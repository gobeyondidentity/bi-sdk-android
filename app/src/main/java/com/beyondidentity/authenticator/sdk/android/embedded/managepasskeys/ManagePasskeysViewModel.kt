package com.beyondidentity.authenticator.sdk.android.embedded.managepasskeys

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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class ManagePasskeysViewModel : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e("Caught $exception")
    }

    var state: ManagePasskeyState by mutableStateOf(ManagePasskeyState())
        private set

    fun onGetPasskeys() {
        resetResult { _, result, progress ->
            state = state.copy(
                getPasskeyResult = result,
                getPasskeyProgress = progress,
            )
        }

        EmbeddedSdk.getPasskeys()
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onEach { result ->
                result.onSuccess { passkeyList ->
                    val message = passkeyList.toIndentString()
                    Timber.d("Got passkeys = $message")
                    state = state.copy(
                        getPasskeyResult = message,
                        getPasskeyProgress = false,
                    )
                }
                result.onFailure {
                    Timber.d("Getting passkeys failed.")
                    state = state.copy(
                        getPasskeyResult = it.toString(),
                        getPasskeyProgress = false,
                    )
                }
            }
            .catch { error ->
                val message = "Getting passkeys failed ${error.message}"
                Timber.d(message)
                state = state.copy(
                    getPasskeyResult = message,
                    getPasskeyProgress = false,
                )
            }
            .launchIn(viewModelScope)
    }

    fun onDeletePasskeyTextChange(text: String) {
        state = state.copy(deletePasskey = text)
    }

    fun onDeletePasskeys() {
        if (!resetResult(
                state.deletePasskey,
                "Please enter a passkey id to delete",
            ) { _, result, progress ->
                state = state.copy(
                    deletePasskeyResult = result,
                    deletePasskeyProgress = progress,
                )
            }
        ) {
            return
        }

        EmbeddedSdk.deletePasskey(state.deletePasskey)
            .flowOn(Dispatchers.Main + coroutineExceptionHandler)
            .onEach { result ->
                result.onSuccess {
                    Timber.d("Deleted passkeys for id: ${state.deletePasskey}")
                    state = state.copy(
                        deletePasskeyResult = "Deleted passkeys for id: ${state.deletePasskey}",
                        deletePasskeyProgress = false,
                        getPasskeyResult = "[]",
                        getPasskeyProgress = false,
                    )
                }
                result.onFailure {
                    Timber.d("Passkeys deletion failed.")
                    state = state.copy(
                        deletePasskeyResult = it.toString(),
                        deletePasskeyProgress = false,
                    )
                }
            }
            .catch { error ->
                val message = "Passkeys deletion failed ${error.message}"
                Timber.d(message)
                state = state.copy(
                    deletePasskeyResult = message,
                    deletePasskeyProgress = false,
                )
            }
            .launchIn(viewModelScope)
    }
}
