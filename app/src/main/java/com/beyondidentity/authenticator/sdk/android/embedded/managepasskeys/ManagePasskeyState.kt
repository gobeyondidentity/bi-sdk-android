package com.beyondidentity.authenticator.sdk.android.embedded.managepasskeys

data class ManagePasskeyState(
    val getPasskeyResult: String = "",
    val getPasskeyProgress: Boolean = false,
    val deletePasskey: String = "",
    val deletePasskeyResult: String = "",
    val deletePasskeyProgress: Boolean = false
)
