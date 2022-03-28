package com.beyondidentity.embedded.sdk.extend

import com.beyondidentity.embedded.sdk.models.ExtendResponse

/**
 * Status of Extending Credentials
 */
interface ExtendCredentialListener {
    /**
     * On new [ExtendResponse] token generated
     *
     * @param token  A random 9 digit token associated with Credentials exported
     */
    fun onUpdate(token: ExtendResponse?)

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
