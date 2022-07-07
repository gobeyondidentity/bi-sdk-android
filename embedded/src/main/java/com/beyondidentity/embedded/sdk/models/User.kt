package com.beyondidentity.embedded.sdk.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val externalId: String,
    val email: String,
    val userName: String,
    val displayName: String,
)
