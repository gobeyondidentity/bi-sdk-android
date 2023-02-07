package com.beyondidentity.embedded.sdk

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.beyondidentity.embedded.sdk.models.AuthenticateResponse
import com.beyondidentity.embedded.sdk.models.BindPasskeyResponse
import com.beyondidentity.embedded.sdk.models.Passkey
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
        private const val TEST_BIND_PASSKEY_URL =
            "https://auth-us.beyondidentity.com/v1/tenants/0123456789ABCDEF/realms/0123456789ABCDEF/identities/0123456789ABCDEF/credential-binding-jobs/01234567-89AB-CDEF-0123-456789ABCDEF:invokeAuthenticator?token=0123456789ABCDEF"
        private const val TEST_PASSKEY_ID =
            "01234567-89AB-CDEF-0123-456789ABCDEF"
    }

    @Before
    fun setUp() {
        if (Timber.forest().isEmpty()) Timber.plant(Timber.DebugTree())

        EmbeddedSdk.init(
            app = ApplicationProvider.getApplicationContext(),
            keyguardPrompt = null,
            logger = { Timber.d(it) },
        )
    }

    //region BindPasskey
    private suspend fun bindPasskey(
        url: String,
    ): Result<BindPasskeyResponse> = suspendCoroutine { continuation ->
        EmbeddedSdk.bindPasskey(url) { continuation.resume(it) }
    }

    /**
     * Test [EmbeddedSdk.bindPasskey] with [TEST_BIND_PASSKEY_URL]
     * Note: This is not a valid bind passkey url, so it will fail
     **/
    @Test
    fun bindPasskey_callback_failure() = runTest {
        Timber.d("~~~ bindPasskey_callback_failure ~~~")

        val result = bindPasskey(TEST_BIND_PASSKEY_URL)
        assertTrue(result.isFailure)
    }
    //endregion BindPasskey

    //region Authenticate
    private suspend fun authenticate(
        url: String,
        passkeyId: String,
    ): Result<AuthenticateResponse> = suspendCoroutine { continuation ->
        EmbeddedSdk.authenticate(url, passkeyId) { continuation.resume(it) }
    }

    /**
     * Test [EmbeddedSdk.authenticate] with [TEST_AUTHENTICATE_URL]
     * Note: This is not a valid authenticate url, so it will fail
     **/
    @Test
    fun authenticate_callback_failure() = runTest {
        Timber.d("~~~ authenticate_callback_failure ~~~")

        val result = authenticate(TEST_AUTHENTICATE_URL, TEST_PASSKEY_ID)
        assertTrue(result.isFailure)
    }
    //endregion Authenticate

    //region GetPasskeys
    private suspend fun getPasskeys(): Result<List<Passkey>> = suspendCoroutine { continuation ->
        EmbeddedSdk.getPasskeys { continuation.resume(it) }
    }

    /**
     * Test [EmbeddedSdk.getPasskeys]
     **/
    @Test
    fun getPasskeys_callback_success() = runTest {
        Timber.d("~~~ getPasskeys_callback_success ~~~")

        val result = getPasskeys()
        assertTrue(result.isSuccess)
        result.onSuccess { passkeyList ->
            assertTrue(passkeyList.isEmpty())
        }
    }
    //endregion GetPasskeys

    //region DeletePasskey
    private suspend fun deletePasskey(id: String): Result<Unit> = suspendCoroutine { continuation ->
        EmbeddedSdk.deletePasskey(id) { continuation.resume(it) }
    }

    /**
     * Test [EmbeddedSdk.deletePasskey] with [TEST_PASSKEY_ID]
     **/
    @Test
    fun deletePasskey_callback_success() = runTest {
        Timber.d("~~~ deletePasskey_callback_success ~~~")

        val result = deletePasskey(TEST_PASSKEY_ID)
        assertTrue(result.isSuccess)
    }
    //endregion DeletePasskey

    //region IsBindPasskeyUrl
    /**
     * Test [EmbeddedSdk.isBindPasskeyUrl] with [TEST_BIND_PASSKEY_URL]
     **/
    @Test
    fun isBindPasskeyUrlTest_Success() {
        Timber.d("~~~ isBindPasskeyUrlTest_Success ~~~")

        // Test Bind Passkey Url
        val bindPasskeyResult = EmbeddedSdk.isBindPasskeyUrl(TEST_BIND_PASSKEY_URL)
        Timber.d("TEST_BIND_PASSKEY_URL: $bindPasskeyResult")
        assertTrue(
            "Bind Passkey Url Test: TEST_BIND_PASSKEY_URL: $bindPasskeyResult",
            bindPasskeyResult,
        )
    }

    /**
     * Test [EmbeddedSdk.isBindPasskeyUrl] with [TEST_AUTHENTICATE_URL]
     **/
    @Test
    fun isBindPasskeyUrlTest_Failure() {
        Timber.d("~~~ isBindPasskeyUrlTest_Failure ~~~")

        // Test Authenticate Url
        val authenticateResult = EmbeddedSdk.isBindPasskeyUrl(TEST_AUTHENTICATE_URL)
        Timber.d("TEST_AUTHENTICATE_URL: $authenticateResult")
        assertFalse(
            "Bind Passkey Url Test: TEST_AUTHENTICATE_URL: $authenticateResult",
            authenticateResult,
        )
    }
    //endregion IsBindPasskeyUrl

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
     * Test [EmbeddedSdk.isAuthenticateUrl] with [TEST_BIND_PASSKEY_URL]
     **/
    @Test
    fun isAuthenticateUrlTest_Failure() {
        Timber.d("~~~ isAuthenticateUrlTest_Failure ~~~")

        // Test Bind Passkey Url
        val bindPasskeyResult = EmbeddedSdk.isAuthenticateUrl(TEST_BIND_PASSKEY_URL)
        Timber.d("TEST_BIND_PASSKEY_URL: $bindPasskeyResult")
        assertFalse(
            "Authenticate Url Test: TEST_BIND_PASSKEY_URL: $bindPasskeyResult",
            bindPasskeyResult,
        )
    }
    //endregion IsAuthenticateUrl
}
