package com.beyondidentity.embedded.sdk.utils

import com.beyondidentity.embedded.sdk.EmbeddedSdk

/**
 * Helper class for projects based on Java
 *
 */
object JavaUtils {

    /**
     * Helper that transforms [kotlin.Result] into [JavaResult] that can be used in java codebase.
     *
     * @param result [kotlin.Result] returned from the [EmbeddedSdk] callbacks
     * @return [JavaResult] representation
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T> toJavaResult(result: Result<T>): JavaResult<T> {
        // It gets double wrapped into Success for some reason
        // Success(Success(T)) or Success(Failure(Throwable))
        try {
            result.onSuccess {
                (it as Result<T>).onSuccess { d ->
                    return JavaResult.success(d)
                }
                (it as Result<T>).onFailure { tr ->
                    return JavaResult.failure(tr)
                }
            }
        } catch (t: ClassCastException) {
            return JavaResult.failure(Throwable("Failed to transform kotlin.Result to JavaResult"))
        }

        return JavaResult.failure(Throwable("Failed to transform kotlin.Result to JavaResult"))
    }
}
