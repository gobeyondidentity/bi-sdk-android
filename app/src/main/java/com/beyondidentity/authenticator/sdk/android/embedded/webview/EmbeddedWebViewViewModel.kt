package com.beyondidentity.authenticator.sdk.android.embedded.webview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class EmbeddedWebViewViewModel : ViewModel() {
    var state: EmbeddedWebViewState by mutableStateOf(EmbeddedWebViewState())
        private set

    private val _events = MutableSharedFlow<EmbeddedWebViewEvents>()
    val events: SharedFlow<EmbeddedWebViewEvents> = _events

    fun onUrlTextChange(text: String) {
        state = state.copy(url = text)
    }

    fun onResultTextChange(text: String) {
        state = state.copy(result = text)
    }

    fun onOverrideUrlLoading() {
        viewModelScope.launch {
            _events.emit(
                EmbeddedWebViewEvents.WebViewSuccess(state.url, state.result)
            )
        }
    }
}
