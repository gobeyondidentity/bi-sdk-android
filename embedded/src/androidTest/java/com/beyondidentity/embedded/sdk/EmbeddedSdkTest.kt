package com.beyondidentity.embedded.sdk

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

@RunWith(AndroidJUnit4::class)
class EmbeddedSdkTest {
    @Before
    fun setup() {
        if (Timber.forest().isEmpty()) Timber.plant(Timber.DebugTree())

        EmbeddedSdk.init(
            app = ApplicationProvider.getApplicationContext(),
            keyguardPrompt = null,
            logger = { log ->
                Timber.d(log)
            }
        )
    }

    @Test
    fun getCredentials_callback_success() {
        EmbeddedSdk.getCredentials { result ->
            assertTrue(result.isSuccess)
            result.onSuccess { credList ->
                assertTrue(credList.isEmpty())
            }
        }
    }

    private val TEST_AUTHENTICATE_URL =
        "https://auth.thisurldoesntmatter.com/bi-authenticate?request=0123456789ABCDEF"
    private val TEST_BIND_CREDENTIAL_URL =
        "https://auth-us.beyondidentity.run/v1/tenants/0123456789ABCDEF/realms/0123456789ABCDEF/identities/0123456789ABCDEF/credential-binding-jobs/01234567-89AB-CDEF-0123-456789ABCDEF:invokeAuthenticator?token=0123456789ABCDEF"

    /**
     * Test [EmbeddedSdk.isAuthenticateUrl] with:
     * - Test Authenticate Url
     * - Test Bind Credential Url
     */
    @Test
    fun isAuthenticateUrlTest() {
        Timber.d("~~~ isAuthenticateUrlTest ~~~")

        // Test Authenticate Url
        val authenticateResult = runIsAuthenticateUrl(TEST_AUTHENTICATE_URL)
        Timber.d("TEST_AUTHENTICATE_URL: $authenticateResult")
        assertTrue(
            "Authenticate Url Test: TEST_AUTHENTICATE_URL: $authenticateResult",
            authenticateResult
        )

        // Test Bind Credential Url
        val bindCredentialResult = runIsAuthenticateUrl(TEST_BIND_CREDENTIAL_URL)
        Timber.d("TEST_BIND_CREDENTIAL_URL: $bindCredentialResult")
        assertFalse(
            "Authenticate Url Test: TEST_BIND_CREDENTIAL_URL: $bindCredentialResult",
            bindCredentialResult
        )
    }

    /**
     * Test [EmbeddedSdk.isBindCredentialUrl] with:
     * - Test Authenticate Url
     * - Test Bind Credential Url
     */
    @Test
    fun isBindCredentialUrlTest() {
        Timber.d("~~~ isBindCredentialUrlTest ~~~")

        // Test Authenticate Url
        val authenticateResult = runIsBindCredentialUrl(TEST_AUTHENTICATE_URL)
        Timber.d("TEST_AUTHENTICATE_URL: $authenticateResult")
        assertFalse(
            "Bind Credential Url Test: TEST_AUTHENTICATE_URL: $authenticateResult",
            authenticateResult
        )

        // Test Bind Credential Url
        val bindCredentialResult = runIsBindCredentialUrl(TEST_BIND_CREDENTIAL_URL)
        Timber.d("TEST_BIND_CREDENTIAL_URL: $bindCredentialResult")
        assertTrue(
            "Bind Credential Url Test: TEST_BIND_CREDENTIAL_URL: $bindCredentialResult",
            bindCredentialResult
        )
    }

    private fun runIsAuthenticateUrl(url: String): Boolean =
        EmbeddedSdk.isAuthenticateUrl(url)

    private fun runIsBindCredentialUrl(url: String): Boolean =
        EmbeddedSdk.isBindCredentialUrl(url)
}
