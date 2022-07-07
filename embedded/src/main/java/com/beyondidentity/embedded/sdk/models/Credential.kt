package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.AuthNCredentialResponse

/**
 * Represent User's credential, wrapper for X.509 Certificate
 *
 * @property id The Globally unique ID of this Credential.
 * @property localCreated The time when this credential was created locally. This could be different from "created" which is the time when this credential was created on the server.
 * @property localUpdated The last time when this credential was updated locally. This could be different from "updated" which is the last time when this credential was updated on the server.
 * @property apiBaseURL The base url for all binding & auth requests
 * @property tenantId The Identity's Tenant.
 * @property realmId The Identity's Realm.
 * @property identityId The Identity that owns this Credential.
 * @property keyHandle Associated key handle.
 * @property state The current state of this credential
 * @property created The time this credential was created.
 * @property updated The last time this credential was updated.
 * @property realm Realm information associated with this credential.
 * @property identity Identity information associated with this credential.
 * @property theme Theme information associated with this credential
 */
data class Credential(
    val id: CredentialID,
    val localCreated: String,
    val localUpdated: String,
    val apiBaseURL: String,
    val tenantId: TenantID,
    val realmId: RealmID,
    val identityId: IdentityID,
    val keyHandle: KeyHandle,
    val state: CredentialState,
    val created: String,
    val updated: String,
    val realm: Realm,
    val identity: Identity,
    val theme: Theme,
) {
    companion object {
        fun from(coreAuthNCredential: AuthNCredentialResponse) =
            Credential(
                id = coreAuthNCredential.id,
                localCreated = coreAuthNCredential.localCreated,
                localUpdated = coreAuthNCredential.localUpdated,
                apiBaseURL = coreAuthNCredential.apiBaseUrl,
                tenantId = coreAuthNCredential.tenantId,
                realmId = coreAuthNCredential.realmId,
                identityId = coreAuthNCredential.identityId,
                keyHandle = coreAuthNCredential.keyHandle,
                state = CredentialState.from(coreAuthNCredential.state),
                created = coreAuthNCredential.created,
                updated = coreAuthNCredential.updated,
                realm = Realm(
                    displayName = coreAuthNCredential.realm.displayName,
                ),
                identity = Identity(
                    displayName = coreAuthNCredential.identity.displayName,
                    username = coreAuthNCredential.identity.username,
                ),
                theme = Theme(
                    logoUrlLight = coreAuthNCredential.theme.logoUrlLight,
                    logoUrlDark = coreAuthNCredential.theme.logoUrlDark,
                    supportUrl = coreAuthNCredential.theme.supportUrl,
                ),
            )
    }
}

/**
 * The Globally unique ID of a Credential.
 */
typealias CredentialID = String

/**
 * The Identity that owns a Credential.
 */
typealias IdentityID = String

/**
 * Associated key handle.
 */
typealias KeyHandle = String

/**
 * The Identity's Realm.
 */
typealias RealmID = String

/**
 * The Identity's Tenant.
 */
typealias TenantID = String

/**
 * State of given [Credential]
 */
enum class CredentialState {
    /**
     * Credential is active
     */
    ACTIVE {
        override fun toString(): String {
            return "active"
        }
    },

    /**
     * Credential is revoked
     */
    REVOKED {
        override fun toString(): String {
            return "revoked"
        }
    },

    /**
     * Unable to determine the state of the credential
     */
    UNKNOWN {
        override fun toString(): String {
            return "unknown"
        }
    };

    companion object {
        fun from(state: String): CredentialState =
            when (state.lowercase()) {
                "active" -> ACTIVE
                "revoked" -> REVOKED
                else -> UNKNOWN
            }
    }
}

/**
 * Realm information associated with a credential.
 *
 * @property displayName The display name of the realm.
 */
data class Realm(
    val displayName: String,
)

/**
 * Identity information associated with a credential.
 *
 * @property displayName The display name of the identity.
 * @property username The username of the identity.
 */
data class Identity(
    val displayName: String,
    val username: String,
)

/**
 * Theme associated with a credential.
 *
 * @property logoUrlLight URL to for resolving the logo image for light mode.
 * @property logoUrlDark URL to for resolving the logo image for dark mode.
 * @property supportUrl URL for customer support portal.
 */
data class Theme(
    val logoUrlLight: String,
    val logoUrlDark: String,
    val supportUrl: String,
)
