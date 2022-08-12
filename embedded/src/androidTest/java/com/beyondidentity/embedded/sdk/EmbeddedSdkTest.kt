package com.beyondidentity.embedded.sdk

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.beyondidentity.embedded.sdk.models.AuthenticateResponse
import com.beyondidentity.embedded.sdk.models.BindCredentialResponse
import com.beyondidentity.embedded.sdk.models.Credential
import com.beyondidentity.embedded.sdk.models.MockOnSelectCredential
import com.beyondidentity.embedded.sdk.models.OnSelectCredential
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class EmbeddedSdkTest {
    companion object {
        private const val TEST_AUTHENTICATE_URL =
            "https://auth-us.beyondidentity.com/bi-authenticate?request=0123456789ABCDEF"
        private const val TEST_BIND_CREDENTIAL_URL =
            "https://auth-us.beyondidentity.com/v1/tenants/0123456789ABCDEF/realms/0123456789ABCDEF/identities/0123456789ABCDEF/credential-binding-jobs/01234567-89AB-CDEF-0123-456789ABCDEF:invokeAuthenticator?token=0123456789ABCDEF"
        private const val TEST_CREDENTIAL_ID =
            "01234567-89AB-CDEF-0123-456789ABCDEF"
    }

    @Before
    fun setUp() {
        if (Timber.forest().isEmpty()) Timber.plant(Timber.DebugTree())

        EmbeddedSdk.init(
            app = ApplicationProvider.getApplicationContext(),
            keyguardPrompt = null,
            logger = { log ->
                Timber.d(log)
            },
        )
    }

    //region BindCredential
    private suspend fun bindCredential(
        url: String,
    ): Result<BindCredentialResponse> = suspendCoroutine { cont ->
        EmbeddedSdk.bindCredential(url) { cont.resume(it) }
    }

    /**
     * Test [EmbeddedSdk.bindCredential] with [TEST_BIND_CREDENTIAL_URL]
     * Note: This is not a valid bind credential url, so it will fail
     **/
    @Test
    fun bindCredential_callback_failure() = runTest {
        Timber.d("~~~ bindCredential_callback_failure ~~~")

        val result = bindCredential(TEST_BIND_CREDENTIAL_URL)
        assertTrue(result.isFailure)
    }
    //endregion BindCredential

    //region Authenticate
    private suspend fun authenticate(
        url: String,
        onSelectCredential: OnSelectCredential,
    ): Result<AuthenticateResponse> = suspendCoroutine { cont ->
        EmbeddedSdk.authenticate(url, onSelectCredential) { cont.resume(it) }
    }

    /**
     * Test [EmbeddedSdk.authenticate] with [TEST_AUTHENTICATE_URL]
     * Note: This is not a valid authenticate url, so it will fail
     **/
    @Test
    fun authenticate_callback_failure() = runTest {
        Timber.d("~~~ authenticate_callback_failure ~~~")

        val result = authenticate(TEST_AUTHENTICATE_URL, MockOnSelectCredential.mock)
        assertTrue(result.isFailure)
    }
    //endregion Authenticate

    //region GetCredentials
    private suspend fun getCredentials(): Result<List<Credential>> =
        suspendCoroutine { cont -> EmbeddedSdk.getCredentials { cont.resume(it) } }

    /**
     * Test [EmbeddedSdk.getCredentials]
     **/
    @Test
    fun getCredentials_callback_success() = runTest {
        Timber.d("~~~ getCredentials_callback_success ~~~")

        val result = getCredentials()
        assertTrue(result.isSuccess)
        result.onSuccess { credentials ->
            assertTrue(credentials.isEmpty())
        }
    }
    //endregion GetCredentials

    //region DeleteCredential
    private suspend fun deleteCredential(id: String): Result<Unit> =
        suspendCoroutine { cont -> EmbeddedSdk.deleteCredential(id) { cont.resume(it) } }

    /**
     * Test [EmbeddedSdk.deleteCredential] with [TEST_CREDENTIAL_ID]
     **/
    @Test
    fun deleteCredential_callback_success() = runTest {
        Timber.d("~~~ deleteCredential_callback_success ~~~")

        val result = deleteCredential(TEST_CREDENTIAL_ID)
        assertTrue(result.isSuccess)
    }
    //endregion DeleteCredential

    //region IsBindCredentialUrl
    /**
     * Test [EmbeddedSdk.isBindCredentialUrl] with [TEST_BIND_CREDENTIAL_URL]
     **/
    @Test
    fun isBindCredentialUrlTest_Success() {
        Timber.d("~~~ isBindCredentialUrlTest_Success ~~~")

        // Test Bind Credential Url
        val bindCredentialResult = EmbeddedSdk.isBindCredentialUrl(TEST_BIND_CREDENTIAL_URL)
        Timber.d("TEST_BIND_CREDENTIAL_URL: $bindCredentialResult")
        assertTrue(
            "Bind Credential Url Test: TEST_BIND_CREDENTIAL_URL: $bindCredentialResult",
            bindCredentialResult,
        )
    }

    /**
     * Test [EmbeddedSdk.isBindCredentialUrl] with [TEST_AUTHENTICATE_URL]
     **/
    @Test
    fun isBindCredentialUrlTest_Failure() {
        Timber.d("~~~ isBindCredentialUrlTest_Failure ~~~")

        // Test Authenticate Url
        val authenticateResult = EmbeddedSdk.isBindCredentialUrl(TEST_AUTHENTICATE_URL)
        Timber.d("TEST_AUTHENTICATE_URL: $authenticateResult")
        assertFalse(
            "Bind Credential Url Test: TEST_AUTHENTICATE_URL: $authenticateResult",
            authenticateResult,
        )
    }
    //endregion IsBindCredentialUrl

    //region IsAuthenticateUrl
    /**
     * Test [EmbeddedSdk.isAuthenticateUrl] with [TEST_AUTHENTICATE_URL]
     **/
    @Test
    fun isAuthenticateUrlTest_Success() {
        Timber.d("~~~ isAuthenticateUrlTest_Success ~~~")

        // Test Authenticate Url
        val authenticateResult = EmbeddedSdk.isAuthenticateUrl(TEST_AUTHENTICATE_URL)
        Timber.d("TEST_AUTHENTICATE_URL: $authenticateResult")
        assertTrue(
            "Authenticate Url Test: TEST_AUTHENTICATE_URL: $authenticateResult",
            authenticateResult,
        )
    }

    /**
     * Test [EmbeddedSdk.isAuthenticateUrl] with [TEST_BIND_CREDENTIAL_URL]
     **/
    @Test
    fun isAuthenticateUrlTest_Failure() {
        Timber.d("~~~ isAuthenticateUrlTest_Failure ~~~")

        // Test Bind Credential Url
        val bindCredentialResult = EmbeddedSdk.isAuthenticateUrl(TEST_BIND_CREDENTIAL_URL)
        Timber.d("TEST_BIND_CREDENTIAL_URL: $bindCredentialResult")
        assertFalse(
            "Authenticate Url Test: TEST_BIND_CREDENTIAL_URL: $bindCredentialResult",
            bindCredentialResult,
        )
    }
    //endregion IsAuthenticateUrl
}
