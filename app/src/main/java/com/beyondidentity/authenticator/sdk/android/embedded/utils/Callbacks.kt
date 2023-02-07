package com.beyondidentity.authenticator.sdk.android.embedded.utils

import com.beyondidentity.authenticator.sdk.android.apis.Auth0TokenResponse
import com.beyondidentity.authenticator.sdk.android.apis.OktaV1TokenResponse
import com.beyondidentity.embedded.sdk.models.AuthenticateResponse
import com.beyondidentity.embedded.sdk.models.BindPasskeyResponse
import okhttp3.ResponseBody

typealias Callback = () -> Unit
typealias UpdateStateCallback = (String, String, Boolean) -> Unit
typealias BindPasskeySuccessCallback = (BindPasskeyResponse) -> Unit
typealias BindPasskeyFailureCallback = (Throwable) -> Unit
typealias BindPasskeyErrorCallback = (Throwable) -> Unit
typealias AuthenticateSuccessCallback = (AuthenticateResponse) -> Unit
typealias AuthenticateFailureCallback = (Throwable) -> Unit
typealias AuthenticateErrorCallback = (Throwable) -> Unit
typealias OktaTokenSuccessCallback = (OktaV1TokenResponse?) -> Unit
typealias OktaTokenFailureCallback = (ResponseBody?) -> Unit
typealias Auth0TokenSuccessCallback = (Auth0TokenResponse?) -> Unit
typealias Auth0TokenFailureCallback = (ResponseBody?) -> Unit
