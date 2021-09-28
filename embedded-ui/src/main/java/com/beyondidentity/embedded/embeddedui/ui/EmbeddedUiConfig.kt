package com.beyondidentity.embedded.embeddedui.ui

/**
 * Configuration class helper for Embedded UI module.
 */
object EmbeddedUiConfig {
    lateinit var config: Config

    /**
     * Config model holding client and brand specific data
     *
     * @property appDisplayName Name of the app or brand to be displayed to the users.
     * @property supportUrlOrEmail A link or an email where users can go to for support.
     * @property authenticationData OIDC client specific data.
     */
    data class Config(
        val appDisplayName: String,
        val supportUrlOrEmail: String,
        val authenticationData: AuthenticationData,
    )

    sealed class AuthenticationData {

        /**
         * Use when your OIDC client is configured as public.
         *
         * @property clientId The client ID generated during the OIDC configuration.
         * @property redirectUri URI where the user will be redirected after the authorization has completed. The redirect URI must be one of the URIs passed in the OIDC configuration.
         */
        data class PublicClientData(
            val clientId: String,
            val redirectUri: String,
        ) : AuthenticationData()

        /**
         * Use when OIDC client is configured as confidential
         *
         * @property clientId The client ID generated during the OIDC configuration.
         * @property redirectUri URI where the user will be redirected after the authorization has completed. The redirect URI must be one of the URIs passed in the OIDC configuration.
         * @property scope string list of OIDC scopes used during authentication to authorize access to a user's specific details. Only "openid" is currently supported.
         */
        data class ConfidentialClientData(
            val clientId: String,
            val redirectUri: String,
            val scope: String,
        ) : AuthenticationData()
    }
}
