package com.beyondidentity.authenticator.sdk.android.embedded.getstarted

import android.net.Uri

data class EmbeddedGetStartedState(
    val registerEmail: String = "",
    val registerResult: String = "",
    val recoverEmail: String = "",
    val recoverResult: String = "",
    val bindCredentialUrl: String = "",
    val bindCredentialResult: String = "",
    val authenticateUrl: String = "",
    val authenticateResult: String = "",
    val urlValidationBindCredentialUrl: String = "",
    val validateBindCredentialUrlResult: String = "",
    val urlValidationAuthenticateUrl: String = "",
    val validateAuthenticateUrlResult: String = "",
)

sealed class EmbeddedGetStartedEvents {
    object ManageCredentials : EmbeddedGetStartedEvents()
    data class VisitDocsEvent(val uri: Uri) : EmbeddedGetStartedEvents()
    data class VisitSupportEvent(val uri: Uri) : EmbeddedGetStartedEvents()
    data class BindCredentialEvent(val result: String) : EmbeddedGetStartedEvents()
    data class AuthenticateEvent(val result: String) : EmbeddedGetStartedEvents()
    data class CredentialRegistration(val result: String) : EmbeddedGetStartedEvents()
}
