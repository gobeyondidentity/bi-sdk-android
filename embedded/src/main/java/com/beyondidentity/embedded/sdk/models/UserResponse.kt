package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.CreateUserResponse
import com.beyondidentity.sdk.android.bicore.models.RecoverUserResponse

/**
 * Represent User
 *
 * @property internalId A randomly generated unique ID that Beyond Identity sets for the user. This is needed for updating the user.
 * @property externalId A unique identifier for the user that you set and may be associated with your database. This is needed for recovery.
 * @property email A user's email
 * @property userName A user's user name
 * @property displayName A user's display name
 * @property dateCreated The date the user was created
 * @property dateModified The last date the user was modified
 */
data class UserResponse(
    val internalId: String,
    val externalId: String,
    val email: String,
    val userName: String,
    val displayName: String,
    val dateCreated: String,
    val dateModified: String,
) {
    companion object {
        fun from(coreUser: CreateUserResponse) =
            UserResponse(
                internalId = coreUser.internalId,
                externalId = coreUser.externalId,
                email = coreUser.email,
                userName = coreUser.userName,
                displayName = coreUser.displayName,
                dateCreated = coreUser.dateCreated,
                dateModified = coreUser.dateModified,
            )

        fun from(coreUser: RecoverUserResponse) =
            UserResponse(
                internalId = coreUser.internalId,
                externalId = coreUser.externalId,
                email = coreUser.email,
                userName = coreUser.userName,
                displayName = coreUser.displayName,
                dateCreated = coreUser.dateCreated,
                dateModified = coreUser.dateModified,
            )
    }
}
