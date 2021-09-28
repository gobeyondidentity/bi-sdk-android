package com.beyondidentity.embedded.embeddedui.ui.utils

import com.beyondidentity.embedded.sdk.models.Credential
import com.beyondidentity.embedded.sdk.models.TokenResponse

object BiEventBus {
    private val observers = mutableSetOf<BiObserver>()

    fun registerObserver(observer: BiObserver) {
        observers.add(observer)
    }

    fun unRegisterObserver(observer: BiObserver) {
        observers.remove(observer)
    }

    fun post(event: BiEvent) {
        observers.forEach { observer ->
            observer.onEvent(event)
        }
    }

    fun clearAll() {
        observers.clear()
    }

    sealed class BiEvent {
        /**
         * When the user taps setup or create a credential
         */
        object CredentialSetup : BiEvent()

        /**
         * When the user taps recover credential
         */
        object CredentialRecovery : BiEvent()

        /**
         *When the user registers a credential on the device
         *
         * @property credential The newly registered credential
         */
        data class CredentialRegistered(val credential: Credential) : BiEvent()

        /**
         * When a successful authentication occurs, only used with public clients (no client secret)
         *
         * @property tokenResponse Token to be used for authentication
         */
        data class Authentication(val tokenResponse: TokenResponse) : BiEvent()

        /**
         * When a successful authorization occurs, only used with confidential clients
         *
         * @property authorizationCode To be exchanged for a token
         */
        data class Authorization(val authorizationCode: String) : BiEvent()

        /**
         * When a user taps delete credential
         */
        object DeleteCredential : BiEvent()

        /**
         * When credential is successfully deleted
         */
        object CredentialDeleted : BiEvent()

        /**
         * When an error occurs
         *
         * @property throwable Throwable associated with the error
         */
        data class BiEventError(val throwable: Throwable) : BiEvent()
    }

    /**
     * Register a [BiObserver] with [BiEventBus] to observe for events
     */
    interface BiObserver {
        fun onEvent(event: BiEvent)
    }
}
