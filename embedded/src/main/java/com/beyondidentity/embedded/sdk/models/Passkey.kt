package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.AuthNCredentialResponse

/**
 * A Universal Passkey is a public and private key pair. The private key is generated, stored, and never leaves the user’s devices’ hardware root of trust (i.e. Secure Enclave).
 * The public key is sent to the Beyond Identity cloud. The private key cannot be tampered with, viewed, or removed from the device in which it is created unless the user explicitly indicates that the trusted device be removed.
 * Passkeys are cryptographically linked to devices and an Identity. A single device can store multiple passkeys for different users and a single Identity can have multiple passkeys.
 *
 * @property id The Globally unique ID of this passkey.
 * @property localCreated The time when this passkey was created locally. This could be different from "created" which is the time when this passkey was created on the server.
 * @property localUpdated The last time when this passkey was updated locally. This could be different from "updated" which is the last time when this passkey was updated on the server.
 * @property apiBaseUrl The base url for all binding & auth requests
 * @property keyHandle Associated key handle.
 * @property state The current state of this passkey
 * @property created The time this passkey was created.
 * @property updated The last time this passkey was updated.
 * @property tenant Tenant information associated with this passkey.
 * @property realm Realm information associated with this passkey.
 * @property identity Identity information associated with this passkey.
 * @property theme Theme information associated with this passkey
 */
data class Passkey(
    val id: PasskeyId,
    val localCreated: String,
    val localUpdated: String,
    val apiBaseUrl: String,
    val keyHandle: KeyHandle,
    val state: State,
    val created: String,
    val updated: String,
    val tenant: Tenant,
    val realm: Realm,
    val identity: Identity,
    val theme: Theme,
) {
    companion object {
        fun from(coreAuthNCredential: AuthNCredentialResponse) =
            Passkey(
                id = coreAuthNCredential.id,
                localCreated = coreAuthNCredential.localCreated,
                localUpdated = coreAuthNCredential.localUpdated,
                apiBaseUrl = coreAuthNCredential.apiBaseUrl,
                keyHandle = coreAuthNCredential.keyHandle,
                state = State.from(coreAuthNCredential.state),
                created = coreAuthNCredential.created,
                updated = coreAuthNCredential.updated,
                tenant = Tenant(
                    id = coreAuthNCredential.tenantId,
                    displayName = coreAuthNCredential.tenant.displayName,
                ),
                realm = Realm(
                    id = coreAuthNCredential.realmId,
                    displayName = coreAuthNCredential.realm.displayName,
                ),
                identity = Identity(
                    id = coreAuthNCredential.identityId,
                    displayName = coreAuthNCredential.identity.displayName,
                    username = coreAuthNCredential.identity.username,
                    primaryEmailAddress = coreAuthNCredential.identity.primaryEmailAddress,
                ),
                theme = Theme(
                    logoLightUrl = coreAuthNCredential.theme.logoUrlLight,
                    logoDarkUrl = coreAuthNCredential.theme.logoUrlDark,
                    supportUrl = coreAuthNCredential.theme.supportUrl,
                ),
            )
    }
}

/**
 * The The unique identifier of the Realm.
 */
typealias RealmId = String

/**
 * The The unique identifier of the Tenant.
 */
typealias TenantId = String

/**
 * The The unique identifier of the Identity.
 */
typealias IdentityId = String

/**
 * The Globally unique ID of a passkey.
 */
typealias PasskeyId = String

/**
 * Associated key handle.
 */
typealias KeyHandle = String

/**
 * State of a given [Passkey].
 */
enum class State {
    /**
     * Passkey is active
     */
    ACTIVE {
        override fun toString(): String {
            return "active"
        }
    },

    /**
     * Passkey is revoked
     */
    REVOKED {
        override fun toString(): String {
            return "revoked"
        }
    };

    companion object {
        fun from(state: String): State =
            when (state.lowercase()) {
                "active" -> ACTIVE
                "revoked" -> REVOKED
                else -> throw Exception("Cannot initialize State from invalid String value $state")
            }
    }
}

/**
 * Tenant information associated with a [Passkey].
 * A Tenant represents an organization in the Beyond Identity Cloud and serves as a root container for all other cloud components in your configuration.
 *
 * @property id The unique identifier of the tenant.
 * @property displayName The display name of the tenant.
 */
data class Tenant(
    val id: TenantId,
    val displayName: String,
)

/**
 * Realm information associated with a [Passkey].
 * A Realm is a unique administrative domain within a `Tenant`.
 * Some Tenants will only need the use of a single Realm, in this case a Realm and a Tenant may seem synonymous.
 * Each Realm contains a unique set of Directory, Policy, Event, Application, and Branding objects.
 *
 * @property id The unique identifier of the realm.
 * @property displayName The display name of the realm.
 */
data class Realm(
    val id: RealmId,
    val displayName: String,
)

/**
 * Identity information associated with a [Passkey].
 * An Identity is a unique identifier that may be used by an end-user to gain access governed by Beyond Identity.
 * An Identity is created at the Realm level.
 * An end-user may have multiple identities. A Realm can have many Identities.
 *
 * @property id The unique identifier of the identity.
 * @property displayName The display name of the identity.
 * @property username The username of the identity.
 * @property primaryEmailAddress The primary email address of the identity.
 */
data class Identity(
    val id: IdentityId,
    val displayName: String,
    val username: String,
    val primaryEmailAddress: String?,
)

/**
 * Theme associated with a [Passkey].
 *
 * @property logoLightUrl URL for resolving the logo image in light mode.
 * @property logoDarkUrl URL for resolving the logo image in dark mode.
 * @property supportUrl URL for customer support portal.
 */
data class Theme(
    val logoLightUrl: String,
    val logoDarkUrl: String,
    val supportUrl: String,
)
