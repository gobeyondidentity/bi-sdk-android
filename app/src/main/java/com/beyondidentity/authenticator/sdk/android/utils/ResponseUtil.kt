package com.beyondidentity.authenticator.sdk.android.utils

import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber

object ResponseUtil {
    fun <T> onResponse(
        method: String,
        response: Response<T>,
        onSuccessResponse: ((T?) -> Unit)?,
        onFailureResponse: ((ResponseBody?) -> Unit)?
    ) {
        if (response.isSuccessful) {
            onSuccessResponse?.invoke(response.body())
        } else {
            onFailureResponse?.invoke(response.errorBody())
        }

        Timber.d(
            "got result for $method = ${
                response.body() ?: response.errorBody()?.string()
            }"
        )
    }
}
