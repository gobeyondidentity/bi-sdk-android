package com.beyondidentity.authenticator.sdk.android.embedded.getstarted

import android.net.Uri

data class EmbeddedGetStartedState(
    val registerUsername: String = "",
    val registerResult: String = "",
    val recoverUsername: String = "",
    val recoverResult: String = "",
    val bindCredentialUrl: String = "",
    val bindCredentialResult: String = "",
)

sealed class EmbeddedGetStartedEvents {
    object ManageCredentials : EmbeddedGetStartedEvents()
    object Authenticate : EmbeddedGetStartedEvents()
    object UrlValidation : EmbeddedGetStartedEvents()
    data class BindCredentialEvent(val result: String) : EmbeddedGetStartedEvents()
    data class VisitDocsEvent(val uri: Uri) : EmbeddedGetStartedEvents()
    data class VisitSupportEvent(val uri: Uri) : EmbeddedGetStartedEvents()
}
