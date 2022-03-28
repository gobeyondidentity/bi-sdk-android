package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.embedded.sdk.models.CredentialState.ACTIVE
import com.beyondidentity.sdk.android.bicore.models.CredentialResponse
import com.beyondidentity.sdk.android.bicore.models.ProfileResponse as CoreProfile

/**
 * Represent User's credential, wrapper for X.509 Certificate
 *
 * @property created  The date the `Credential` was created.
 * @property handle The handle for the `Credential`.
 * @property keyHandle The keystore key handle.
 * @property name The display name of the `Credential`.
 * @property imageUrl The uri of your company or app's logo.
 * @property chain The certificate chain of the `Credential`.
 * @property rootFingerprint The SHA256 hash of the root certificate as a base64 encoded string.
 * @property loginUri The uri of your app's sign in screen. This is where the user would authenticate into your app.
 * @property enrollUri The uri of your app's sign up screen. This is where the user would register with your service.
 * @property state Current state of the `Credential`
 */
data class Credential(
    val created: String,
    val handle: String,
    val keyHandle: String,
    val name: String,
    val imageUrl: String,
    val chain: List<String>,
    val rootFingerprint: String,
    val loginUri: String? = null,
    val enrollUri: String? = null,
    val state: CredentialState,
) {
    companion object {
        fun from(coreCredential: CredentialResponse): Credential {
            var isValidChain = true
            val chain = coreCredential.chain.map {
                it.error?.let { isValidChain = false }
                it.value ?: ""
            }
            // If any IntegrityResult contains an error, credential is invalid
            val status = if (
                coreCredential.handle.error != null ||
                coreCredential.keyHandle.error != null ||
                coreCredential.created.error != null ||
                coreCredential.rootFingerprint.error != null ||
                !isValidChain
            ) {
                CredentialState.INVALID
            } else {
                CredentialState.from(coreCredential.state)
            }

            return Credential(
                created = coreCredential.created.value ?: "",
                handle = coreCredential.handle.value ?: "",
                keyHandle = coreCredential.keyHandle.value ?: "",
                name = coreCredential.name,
                imageUrl = coreCredential.imageUrl,
                chain = chain,
                rootFingerprint = coreCredential.rootFingerprint.value ?: "",
                loginUri = coreCredential.loginUri,
                enrollUri = coreCredential.enrollUri,
                state = status,
            )
        }

        fun from(coreProfile: CoreProfile) =
            Credential(
                created = coreProfile.created,
                handle = coreProfile.handle,
                keyHandle = coreProfile.keyHandle,
                name = coreProfile.name,
                imageUrl = coreProfile.imageUrl,
                chain = coreProfile.chain,
                rootFingerprint = coreProfile.rootFingerprint,
                loginUri = coreProfile.loginUri,
                enrollUri = coreProfile.enrollUri,
                state = ACTIVE,
            )
    }
}

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
     * Device has been deleted
     */
    DEVICE_DELETED {
        override fun toString(): String {
            return "deviceDeleted"
        }
    },
    /**
     * One or more fields failed their integrity checks
     */
    INVALID {
        override fun toString(): String {
            return "invalid"
        }
    },
    /**
     * User has been deleted
     */
    USER_DELETED {
        override fun toString(): String {
            return "userDeleted"
        }
    },
    /**
     * User is suspended
     */
    USER_SUSPENDED {
        override fun toString(): String {
            return "userSuspended"
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
                "devicedeleted" -> DEVICE_DELETED
                "userdeleted" -> USER_DELETED
                "usersuspended" -> USER_SUSPENDED
                "invalid" -> INVALID
                else -> UNKNOWN
            }
    }
}
