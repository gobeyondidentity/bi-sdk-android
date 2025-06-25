package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.BindCredentialResponse as BiBindCredentialResponse
import com.beyondidentity.sdk.android.bicore.models.UrlDataResponse

/**
 * A response returned after successfully binding a passkey to a device.
 *
 * @property passkey The `Passkey` bound to the device.
 * @property postBindingRedirectUri A URI that can be redirected to once a passkey is bound. This could be a URI that automatically logs the user in with the newly bound passkey, or a success page indicating that a passkey has been bound.
 */
data class BindPasskeyResponse(val passkey: Passkey, val postBindingRedirectUri: String? = null) {
    companion object {
        fun from(bindCredentialResponse: BiBindCredentialResponse) = BindPasskeyResponse(
            passkey = Passkey.from(bindCredentialResponse.credential),
            postBindingRedirectUri = bindCredentialResponse.postBindingRedirectUri
        )

        fun from(urlDataResponse: UrlDataResponse) = urlDataResponse.bindCredential?.let { bindCredentialResponse ->
            from(bindCredentialResponse)
        }
    }
}
