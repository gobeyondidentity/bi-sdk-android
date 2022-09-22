package com.beyondidentity.embedded.sdk.models

import com.beyondidentity.sdk.android.bicore.models.AuthNCredentialResponse
import com.beyondidentity.sdk.android.bicore.models.Identity
import com.beyondidentity.sdk.android.bicore.models.Realm
import com.beyondidentity.sdk.android.bicore.models.Tenant
import com.beyondidentity.sdk.android.bicore.models.Theme
import org.junit.Test
import java.lang.Exception
import com.beyondidentity.sdk.android.bicore.models.BindCredentialResponse as BiBindCredentialResponse

@Suppress("LocalVariableName")
class BindCredentialResponseTest {
    @Test
    fun checkFromBiAuthenticateResponseSuccess() {
        val ID =
            "01234567-89AB-CDEF-0123-456789ABCDEF"
        val LOCAL_CREATED =
            "2022-06-15T12:00:00"
        val LOCAL_UPDATED =
            "2022-06-15T12:00:00"
        val API_BASE_URL =
            "https://auth-us.beyondidentity.com"
        val TENANT_ID =
            "0123456789ABCDEF"
        val REALM_ID =
            "0123456789ABCDEF"
        val IDENTITY_ID =
            "0123456789ABCDEF"
        val KEY_HANDLE =
            "km:0123456789ABCDEF"
        val STATE =
            "Active"
        val CREATED =
            "2022-06-15T12:00:00"
        val UPDATED =
            "2022-06-15T12:00:00"
        val TENANT_DISPLAY_NAME =
            "Beyond Identity"
        val REALM_DISPLAY_NAME =
            "Beyond Identity"
        val IDENTITY_DISPLAY_NAME =
            "Beyond Identity"
        val IDENTITY_USERNAME =
            "Beyond Identity"
        val IDENTITY_PRIMARY_EMAIL_ADDRESS =
            "foo.bar@beyondidentity.com"
        val THEME_LOGO_URL_LIGHT =
            "https://byndid-public-assets.s3-us-west-2.amazonaws.com/logos/beyondidentity.png"
        val THEME_LOGO_URL_DARK =
            "https://byndid-public-assets.s3-us-west-2.amazonaws.com/logos/beyondidentity.png"
        val THEME_SUPPORT_URL =
            "https://www.beyondidentity.com/support"
        val POST_BINDING_DIRECT_URI =
            "https://console-us.beyondidentity.run/api/auth?tenant_id=0123456789ABCDEF&useBI=true"

        val biBindCredentialResponse = BiBindCredentialResponse(
            credential = AuthNCredentialResponse(
                id = ID,
                localCreated = LOCAL_CREATED,
                localUpdated = LOCAL_UPDATED,
                apiBaseUrl = API_BASE_URL,
                tenantId = TENANT_ID,
                realmId = REALM_ID,
                identityId = IDENTITY_ID,
                keyHandle = KEY_HANDLE,
                state = STATE,
                created = CREATED,
                updated = UPDATED,
                tenant = Tenant(
                    displayName = TENANT_DISPLAY_NAME,
                ),
                realm = Realm(
                    displayName = REALM_DISPLAY_NAME,
                ),
                identity = Identity(
                    displayName = IDENTITY_DISPLAY_NAME,
                    username = IDENTITY_USERNAME,
                    primaryEmailAddress = IDENTITY_PRIMARY_EMAIL_ADDRESS,
                ),
                theme = Theme(
                    logoUrlLight = THEME_LOGO_URL_LIGHT,
                    logoUrlDark = THEME_LOGO_URL_DARK,
                    supportUrl = THEME_SUPPORT_URL,
                ),
            ),
            postBindingRedirectUri = POST_BINDING_DIRECT_URI,
        )

        val bindCredentialResponse = BindCredentialResponse.from(biBindCredentialResponse)

        assert(
            bindCredentialResponse.credential.id.equals(
                biBindCredentialResponse.credential.id,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.localCreated.equals(
                biBindCredentialResponse.credential.localCreated,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.localUpdated.equals(
                biBindCredentialResponse.credential.localUpdated,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.apiBaseURL.equals(
                biBindCredentialResponse.credential.apiBaseUrl,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.tenantId.equals(
                biBindCredentialResponse.credential.tenantId,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.realmId.equals(
                biBindCredentialResponse.credential.realmId,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.identityId.equals(
                biBindCredentialResponse.credential.identityId,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.keyHandle.equals(
                biBindCredentialResponse.credential.keyHandle,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.state.name.equals(
                biBindCredentialResponse.credential.state,
                ignoreCase = true,
            )
        )
        assert(
            bindCredentialResponse.credential.created.equals(
                biBindCredentialResponse.credential.created,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.updated.equals(
                biBindCredentialResponse.credential.updated,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.tenant.displayName.equals(
                biBindCredentialResponse.credential.tenant.displayName,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.realm.displayName.equals(
                biBindCredentialResponse.credential.realm.displayName,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.identity.displayName.equals(
                biBindCredentialResponse.credential.identity.displayName,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.identity.username.equals(
                biBindCredentialResponse.credential.identity.username,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.identity.primaryEmailAddress.equals(
                biBindCredentialResponse.credential.identity.primaryEmailAddress,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.theme.logoUrlLight.equals(
                biBindCredentialResponse.credential.theme.logoUrlLight,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.theme.logoUrlDark.equals(
                biBindCredentialResponse.credential.theme.logoUrlDark,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.credential.theme.supportUrl.equals(
                biBindCredentialResponse.credential.theme.supportUrl,
                ignoreCase = false,
            )
        )
        assert(
            bindCredentialResponse.postBindingRedirectUri.equals(
                biBindCredentialResponse.postBindingRedirectUri,
                ignoreCase = false,
            )
        )
    }

    @Test
    fun checkFromBiAuthenticateResponseInvalidState() {
        val ID =
            "01234567-89AB-CDEF-0123-456789ABCDEF"
        val LOCAL_CREATED =
            "2022-06-15T12:00:00"
        val LOCAL_UPDATED =
            "2022-06-15T12:00:00"
        val API_BASE_URL =
            "https://auth-us.beyondidentity.com"
        val TENANT_ID =
            "0123456789ABCDEF"
        val REALM_ID =
            "0123456789ABCDEF"
        val IDENTITY_ID =
            "0123456789ABCDEF"
        val KEY_HANDLE =
            "km:0123456789ABCDEF"
        val STATE =
            "Foo Bar"
        val CREATED =
            "2022-06-15T12:00:00"
        val UPDATED =
            "2022-06-15T12:00:00"
        val TENANT_DISPLAY_NAME =
            "Beyond Identity"
        val REALM_DISPLAY_NAME =
            "Beyond Identity"
        val IDENTITY_DISPLAY_NAME =
            "Beyond Identity"
        val IDENTITY_USERNAME =
            "Beyond Identity"
        val IDENTITY_PRIMARY_EMAIL_ADDRESS =
            "foo.bar@beyondidentity.com"
        val THEME_LOGO_URL_LIGHT =
            "https://byndid-public-assets.s3-us-west-2.amazonaws.com/logos/beyondidentity.png"
        val THEME_LOGO_URL_DARK =
            "https://byndid-public-assets.s3-us-west-2.amazonaws.com/logos/beyondidentity.png"
        val THEME_SUPPORT_URL =
            "https://www.beyondidentity.com/support"
        val POST_BINDING_DIRECT_URI =
            "https://console-us.beyondidentity.run/api/auth?tenant_id=0123456789ABCDEF&useBI=true"

        val biBindCredentialResponse = BiBindCredentialResponse(
            credential = AuthNCredentialResponse(
                id = ID,
                localCreated = LOCAL_CREATED,
                localUpdated = LOCAL_UPDATED,
                apiBaseUrl = API_BASE_URL,
                tenantId = TENANT_ID,
                realmId = REALM_ID,
                identityId = IDENTITY_ID,
                keyHandle = KEY_HANDLE,
                state = STATE,
                created = CREATED,
                updated = UPDATED,
                tenant = Tenant(
                    displayName = TENANT_DISPLAY_NAME,
                ),
                realm = Realm(
                    displayName = REALM_DISPLAY_NAME,
                ),
                identity = Identity(
                    displayName = IDENTITY_DISPLAY_NAME,
                    username = IDENTITY_USERNAME,
                    primaryEmailAddress = IDENTITY_PRIMARY_EMAIL_ADDRESS,
                ),
                theme = Theme(
                    logoUrlLight = THEME_LOGO_URL_LIGHT,
                    logoUrlDark = THEME_LOGO_URL_DARK,
                    supportUrl = THEME_SUPPORT_URL,
                ),
            ),
            postBindingRedirectUri = POST_BINDING_DIRECT_URI,
        )

        try {
            BindCredentialResponse.from(biBindCredentialResponse)
        } catch (e: Exception) {
            assert(
                "Cannot initialize CredentialState from invalid String value $STATE".equals(
                    e.message,
                    ignoreCase = false,
                )
            )
        }
    }
}
