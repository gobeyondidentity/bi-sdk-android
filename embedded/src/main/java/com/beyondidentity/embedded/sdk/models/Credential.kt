package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.ProfileResponse as CoreProfile

/**
 * Represent User's credential, wrapper for X.509 Certificate
 *
 * @property created  The date the `Credential` was created.
 * @property handle The handle for the `Credential`. This is identical to your `tenant_id`.
 * @property keyHandle The keystore key handle.
 * @property name The display name of the `Credential`.
 * @property imageUrl The uri of your company or app's logo.
 * @property chain The certificate chain of the `Credential`.
 * @property rootFingerprint The SHA256 hash of the root certificate as a base64 encoded string.
 * @property loginUri The uri of your app's sign in screen. This is where the user would authenticate into your app.
 * @property enrollUri The uri of your app's sign up screen. This is where the user would register with your service.
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
) {
    companion object {
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
            )
    }
}
