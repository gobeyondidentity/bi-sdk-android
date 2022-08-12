package com.beyondidentity.embedded.sdk.models

object MockOnSelectedCredential {
    val mock = object : OnSelectedCredential {
        override fun invoke(p1: CredentialID?) {}
    }
}
