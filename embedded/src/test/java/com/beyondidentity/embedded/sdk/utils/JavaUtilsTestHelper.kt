package com.beyondidentity.embedded.sdk.utils

object JavaUtilsTestHelper {
    @JvmStatic
    fun <T> getResultSuccess(data: T, c: (Result<T>) -> Unit) {
        c(Result.success(data))
    }

    @JvmStatic
    fun getResultFailure(throwable: Throwable, c: (Result<Any>) -> Unit) {
        c(Result.failure(throwable))
    }
}
