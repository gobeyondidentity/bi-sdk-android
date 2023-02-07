package com.beyondidentity.authenticator.sdk.android.embedded.getstarted

import android.net.Uri

data class EmbeddedGetStartedState(
    val registerUsername: String = "",
    val registerResult: String = "",
    val registerProgress: Boolean = false,
    val recoverUsername: String = "",
    val recoverResult: String = "",
    val recoverProgress: Boolean = false,
    val bindPasskeyUrl: String = "",
    val bindPasskeyResult: String = "",
    val bindPasskeyProgress: Boolean = false,
)

sealed class EmbeddedGetStartedEvents {
    object ManagePasskeys : EmbeddedGetStartedEvents()
    object Authenticate : EmbeddedGetStartedEvents()
    object UrlValidation : EmbeddedGetStartedEvents()
    data class BindPasskeyEvent(val result: String) : EmbeddedGetStartedEvents()
    data class VisitDocsEvent(val uri: Uri) : EmbeddedGetStartedEvents()
    data class VisitSupportEvent(val uri: Uri) : EmbeddedGetStartedEvents()
}
