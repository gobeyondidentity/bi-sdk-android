package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.UrlDataResponse
import com.beyondidentity.sdk.android.bicore.models.BindCredentialResponse as BiBindCredentialResponse

/**
 * A response returned after successfully binding a credential to a device.
 *
 * @property credential The `Credential` bound to the device.
 * @property postBindingRedirectUri A URI that can be redirected to once a credential is bound. This could be a URI that automatically logs the user in with the newly bound credential, or a success page indicating that a credential has been bound.
 */
data class BindCredentialResponse(
    val credential: Credential,
    val postBindingRedirectUri: String? = null,
) {
    companion object {
        fun from(bindCredentialResponse: BiBindCredentialResponse) =
            BindCredentialResponse(
                credential = Credential.from(bindCredentialResponse.credential),
                postBindingRedirectUri = bindCredentialResponse.postBindingRedirectUri,
            )

        fun from(urlDataResponse: UrlDataResponse) =
            urlDataResponse.bindCredential?.let { bindCredentialResponse ->
                from(bindCredentialResponse)
            }
    }
}
