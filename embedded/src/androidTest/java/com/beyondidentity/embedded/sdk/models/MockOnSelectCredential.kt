package com.beyondidentity.embedded.sdk.models

object MockOnSelectCredential {
    val mock = object : OnSelectCredential {
        override fun invoke(p1: List<Credential>, p2: OnSelectedCredential) {}
    }
}
