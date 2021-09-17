package com.beyondidentity.embedded.sdk.export

import com.beyondidentity.embedded.sdk.models.ExportResponse

/**
 * Status of Exporting Credentials
 */
interface ExportCredentialListener {
    /**
     * On new [ExportResponse] token generated
     *
     * @param token  A random 9 digit token associated with Credentials exported
     */
    fun onUpdate(token: ExportResponse?)

    /**
     * Export complete.
     */
    fun onFinish()

    /**
     * Export failed.
     *
     * @param throwable
     */
    fun onError(throwable: Throwable)
}
