package com.beyondidentity.authenticator.sdk.android.embedded.utils

import com.beyondidentity.authenticator.sdk.android.utils.Auth0TokenResponse
import com.beyondidentity.authenticator.sdk.android.utils.OktaV1TokenResponse
import com.beyondidentity.embedded.sdk.models.AuthenticateResponse
import com.beyondidentity.embedded.sdk.models.BindCredentialResponse
import okhttp3.ResponseBody

typealias Callback = () -> Unit
typealias UpdateStateCallback = (String, String) -> Unit
typealias BindCredentialSuccessCallback = (BindCredentialResponse) -> Unit
typealias BindCredentialFailureCallback = (Throwable) -> Unit
typealias BindCredentialErrorCallback = (Throwable) -> Unit
typealias AuthenticateSuccessCallback = (AuthenticateResponse) -> Unit
typealias AuthenticateFailureCallback = (Throwable) -> Unit
typealias AuthenticateErrorCallback = (Throwable) -> Unit
typealias OktaTokenSuccessCallback = (OktaV1TokenResponse?) -> Unit
typealias OktaTokenFailureCallback = (ResponseBody?) -> Unit
typealias Auth0TokenSuccessCallback = (Auth0TokenResponse?) -> Unit
typealias Auth0TokenFailureCallback = (ResponseBody?) -> Unit
