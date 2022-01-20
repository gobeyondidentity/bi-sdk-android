package com.beyondidentity.embedded.sdk.export

import com.beyondidentity.embedded.sdk.models.ExportResponse

/**
 * Status of Extending Credentials
 */
interface ExportCredentialListener {
    /**
     * On new [ExportResponse] token generated
     *
     * @param token  A random 9 digit token associated with Credentials exported
     */
    fun onUpdate(token: ExportResponse?)

    /**
     * Extend credential complete.
     */
    fun onFinish()

    /**
     * Extending credential failed.
     *
     * @param throwable
     */
    fun onError(throwable: Throwable)
}
