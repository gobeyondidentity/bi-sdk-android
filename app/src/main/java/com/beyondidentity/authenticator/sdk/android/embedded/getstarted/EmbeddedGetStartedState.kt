package com.beyondidentity.authenticator.sdk.android.embedded.getstarted

import android.net.Uri

data class EmbeddedGetStartedState(
    val registerEmail: String = "",
    val registerResult: String = "",
    val recoverEmail: String = "",
    val recoverResult: String = "",
)

sealed class EmbeddedGetStartedEvents {
    object ManageCredentials: EmbeddedGetStartedEvents()
    object ExtendCredentials: EmbeddedGetStartedEvents()
    object Authenticate: EmbeddedGetStartedEvents()
    data class VisitDocsEvent(val uri: Uri): EmbeddedGetStartedEvents()
    data class VisitSupportEvent(val uri: Uri): EmbeddedGetStartedEvents()
    data class CredentialRegistration(val result: String): EmbeddedGetStartedEvents()
}