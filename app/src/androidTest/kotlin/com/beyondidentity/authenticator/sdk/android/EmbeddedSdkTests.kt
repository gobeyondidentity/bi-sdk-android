package com.beyondidentity.authenticator.sdk.android

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedActivity
import com.beyondidentity.authenticator.sdk.android.pages.EmbeddedSdkPage
import com.beyondidentity.authenticator.sdk.android.utils.EmbeddedSdkTestUtils
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import com.beyondidentity.embedded.sdk.models.Passkey
import java.util.Properties
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import timber.log.Timber

class EmbeddedSdkTests {

    companion object {
        private const val DEFAULT_USERNAME = "jetpack_compose_test"
        private const val DEFAULT_USERNAME_1 = "jetpack_compose_test1"
        private const val DEFAULT_USERNAME_2 = "jetpack_compose_test2"
        private const val GARBAGE_URL = "Lorem Ipsum"

        @Suppress("ktlint:standard:max-line-length")
        private const val AUTHENTICATE_URL =
            "http://localhost:8092/bi-authenticate?request=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImprdSI6Imh0dHBzOi8vYXV0aC11cy5iZXlvbmRpZGVudGl0eS5jb20vdjEvdGVuYW50cy8wMDAxZTMyMTRkOTlmNTg1L3JlYWxtcy84YzQyMjNkNDg0YzY4NWRiL2FwcGxpY2F0aW9ucy85MTlmYjU0NS0yM2NiLTQ1NjMtYWRkYS1mMzMxMDVhMjI0M2QvLndlbGwta25vd24vandrcy5qc29uIiwia2lkIjoiMUZCNUFDRjUwQkE0QUE1MUJGOEYyNjg0NTUwNERFOTI5MDc1RkJGM0VBNkY5NDFBMjc3RjJCRTUxNzk2NEU0NTEifQ.eyJpc3MiOiJodHRwczovL2JleW9uZGlkZW50aXR5LmNvbS92MS90ZW5hbnRzLzAwMDFlMzIxNGQ5OWY1ODUvcmVhbG1zLzhjNDIyM2Q0ODRjNjg1ZGIvYXBwbGljYXRpb25zLzkxOWZiNTQ1LTIzY2ItNDU2My1hZGRhLWYzMzEwNWEyMjQzZCIsInN1YiI6IkFNRFdiZ01xaFg0RDgzRGtYNVF2alVYSiIsImF1ZCI6W10sImV4cCI6MTY2NDIyNTEzNiwibmJmIjoxNjY0MjE2NzM2LCJpYXQiOjE2NjQyMjA5MzYsImp0aSI6IjMxYjk3NzBiLThlNGUtNDI0My1iNDdlLTU1MTA3M2Y1YjhmMSIsImNpZCI6IjZmZDhhMGM4LWQwNWQtNDA1YS1iYTdmLWVkNjVlOWRlODI2NSIsInZlcmIiOiJhdXRoZW50aWNhdGUiLCJhdXRobiI6eyJiYXNlX3VybCI6Imh0dHBzOi8vYXV0aC11cy5iZXlvbmRpZGVudGl0eS5jb20iLCJyaWQiOiI4YzQyMjNkNDg0YzY4NWRiIiwidGlkIjoiMDAwMWUzMjE0ZDk5ZjU4NSIsImFpZCI6IjkxOWZiNTQ1LTIzY2ItNDU2My1hZGRhLWYzMzEwNWEyMjQzZCIsImFjdGlvbiI6eyJwcm92ZSI6eyJ0cmFuc2FjdGlvbl9pZCI6ImFDaThFNDdwVHd6cFhhSDh3d0wyOE1hOFg4Y0gyQnl4Iiwibm9uY2UiOiI5T0w5UklZZjVNZUtMZEI0SkJpMVRXaHBiNThPMVlnNktQcktTR0dubm84bTBQN0ZpZy1KNE1sMXhqYWMzMTM5IiwiYmlfcmVxIjoiVzNzaVpHbHlaV04wSWpwN0ltbGtJam9pY0d4aGRHWnZjbTBpTENKaGNtZDFiV1Z1ZEhNaU9sdGRmWDFkIn19LCJyZWRpcmVjdF91cmkiOiJodHRwOi8vZXhhbXBsZS5jb20iLCJzdGF0ZSI6ImZvb2JhciJ9LCJvcmlnaW4iOnsiaXAiOiIxMjcuMC4wLjYiLCJ1YSI6IlJ1YnkiLCJnZW8iOiJVbmtub3duIENpdHkiLCJyZWYiOiIifSwiaW5kaXJlY3RfdXJpIjoiaHR0cHM6Ly9hdXRoLXVzLmJleW9uZGlkZW50aXR5LmNvbS92MS90ZW5hbnRzLzAwMDFlMzIxNGQ5OWY1ODUvcmVhbG1zLzhjNDIyM2Q0ODRjNjg1ZGIvdHJhbnNhY3Rpb25zL2FDaThFNDdwVHd6cFhhSDh3d0wyOE1hOFg4Y0gyQnl4L2luZGlyZWN0In0.Fg2_uhGm82U8Mn9GDIy8dhrzuFa86d8P8fAPeV-NfW4n_jEO6wsC65J0VMMNdTSFL4sOHlz4OT_t9LFwkfRwgF2zuGlcv8GEw16_WTZcvEMXLWKCy_vaAYMOiWpA-Nv3PfvrzD9gJu6lWCl7enySfqmukQuwR4hjxQtWJOUvL2R_v6mm8sreoj7ohbAKjeUIeHun4P6yvO6Bn6g34_LDWbyjEOw5yJBra9udk4KCffmq3R8XvBJXnrgSWxBGAAdm8wn7YJdbycDNs8zfGCiccfLsvWej_Dtq-bFFdA_ZWEQwuWH30UxCS-jjDBqLqH2fexeVWSgVGhzgA4PrJLRfkrlVdjRtP_REHPr8Cbzz06SKDVTNVVjWFXvWWiQSxghi-Xe7hsRAixnHtOUYI0t16atEECX3m0tGaxHWkE3A7sFPihKvJNodjmghKA7WzsDle42eMHP414Ta3CnY5mOv7lbw4QmbIhZe2Fl3vWxue9JaX6ye9akDbJDMu-ly9-1X"

        @Suppress("ktlint:standard:max-line-length")
        private const val BIND_PASSKEY_URL =
            "https://auth-us.beyondidentity.com/v1/tenants/0001e3214d99f585/realms/8c4223d484c685db/identities/534f87b7c4ec9367/credential-binding-jobs/7b9727cd4d8c525d:invokeAuthenticator?token=To9jFj5GkhqDzWtwHFYwO9XFuUKa2kPAScZNfqffJ0TJTSVIZW_pI_TZJ8iD7MDZ"
    }

    @get:Rule
    var composeContentTestRule: ComposeContentTestRule =
        createAndroidComposeRule<EmbeddedGetStartedActivity>()

    @Before
    fun setup() = runTest {
        clearPasskeys()
    }

    private fun clearPasskeys() = runTest {
        Timber.d("Getting passkeys")
        val getPasskeysResult = getPasskeys()
        getPasskeysResult.getOrNull()?.let { passkeys ->
            Timber.d("Found ${passkeys.size} passkeys")
            val size = passkeys.size
            passkeys.forEachIndexed { index, passkey ->
                Timber.d("Deleting passkey with id: ${passkey.id}")
                val deletePasskeyResult = deletePasskey(passkey.id)
                deletePasskeyResult.onSuccess {
                    Timber.d("Passkeys delete success: ${passkey.id}")
                    if (size == index + 1) Timber.d("All passkeys deleted")
                }
                deletePasskeyResult.onFailure {
                    Timber.d(it, "Passkeys delete failure: ${passkey.id}")
                    if (size == index + 1) Timber.d("All passkeys deleted")
                }
            }
        }
    }

    private suspend fun getPasskeys(): Result<List<Passkey>> = suspendCoroutine { continuation ->
        EmbeddedSdk.getPasskeys { continuation.resume(it) }
    }

    private suspend fun deletePasskey(id: String): Result<Unit> = suspendCoroutine { continuation ->
        EmbeddedSdk.deletePasskey(id) { continuation.resume(it) }
    }

    @Test
    @Throws(InterruptedException::class)
    fun bindPasskey_emptyUsername() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.registerPasskey(composeContentTestRule, "")
        EmbeddedSdkPage.verifyRegisterResult(
            composeContentTestRule,
            "Please enter a username",
            false
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun bindPasskey_usernameAlreadyExists() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.registerPasskey(composeContentTestRule, DEFAULT_USERNAME)
        EmbeddedSdkPage.verifyRegisterResult(
            composeContentTestRule,
            "username already exists",
            true
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun bindPasskey() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        val expectedUsername = EmbeddedSdkPage.registerPasskey(composeContentTestRule)

        EmbeddedSdkPage.verifyRegisterResult(
            composeContentTestRule,
            "username = $expectedUsername",
            true
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun recoverPasskey_emptyUsername() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.recoverPasskey(composeContentTestRule, "")
        EmbeddedSdkPage.verifyRecoverResult(
            composeContentTestRule,
            "Please enter a username",
            false
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun recoverPasskey_identityNotFound() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.recoverPasskey(
            composeContentTestRule,
            "nonCreatedPasskey${System.currentTimeMillis()}"
        )
        EmbeddedSdkPage.verifyRecoverResult(
            composeContentTestRule,
            "identity not found",
            true
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun recoverPasskey() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.recoverPasskey(composeContentTestRule, DEFAULT_USERNAME)
        EmbeddedSdkPage.verifyRecoverResult(
            composeContentTestRule,
            "username = $DEFAULT_USERNAME",
            true
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun recoverPasskey_overwritePrevPasskey() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        recoverPasskey()
        recoverPasskey()
        managePasskeys_oneUser()
    }

    @Test
    @Throws(InterruptedException::class)
    fun managePasskeys_noUser() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.navigateToManagePasskeys(composeContentTestRule)
        EmbeddedSdkPage.viewPasskeysInsideManage(composeContentTestRule)
        EmbeddedSdkPage.verifyViewPasskeyResult(
            composeContentTestRule,
            "[\n  \n]",
            false
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun managePasskeys_oneUser() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        recoverPasskey()

        EmbeddedSdkPage.navigateToManagePasskeys(composeContentTestRule)
        EmbeddedSdkPage.viewPasskeysInsideManage(composeContentTestRule)
        EmbeddedSdkPage.verifyViewPasskeyResult(
            composeContentTestRule,
            "username = $DEFAULT_USERNAME",
            true
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun managePasskeys_multipleUser() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        val users = arrayOf(DEFAULT_USERNAME, DEFAULT_USERNAME_1, DEFAULT_USERNAME_2)

        users.forEach {
            EmbeddedSdkPage.recoverPasskey(composeContentTestRule, it)
            EmbeddedSdkPage.verifyRecoverResult(
                composeContentTestRule,
                "username = $it",
                true
            )
        }

        EmbeddedSdkPage.navigateToManagePasskeys(composeContentTestRule)
        EmbeddedSdkPage.viewPasskeysInsideManage(composeContentTestRule)

        users.forEach {
            EmbeddedSdkPage.verifyViewPasskeyResult(
                composeContentTestRule,
                "username = $it",
                true
            )
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun deletePasskeys_noUser() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.navigateToManagePasskeys(composeContentTestRule)
        EmbeddedSdkPage.deletePasskey(composeContentTestRule, "")
        EmbeddedSdkPage.verifyDeletePasskeyResult(
            composeContentTestRule,
            "Please enter a passkey id to delete",
            false
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun deletePasskeys_invalidUser() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.navigateToManagePasskeys(composeContentTestRule)
        EmbeddedSdkPage.deletePasskey(composeContentTestRule, "fake_id")
        EmbeddedSdkPage.verifyDeletePasskeyResult(
            composeContentTestRule,
            "Deleted passkeys for id: fake_id",
            false
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun deletePasskeys_oneUser() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        recoverPasskey()

        EmbeddedSdkPage.navigateToManagePasskeys(composeContentTestRule)

        val passkeyId = EmbeddedSdkTestUtils.getIdByUsername(
            composeContentTestRule,
            DEFAULT_USERNAME
        )

        EmbeddedSdkPage.deletePasskey(
            composeContentTestRule,
            passkeyId
        )
        EmbeddedSdkPage.verifyDeletePasskeyResult(
            composeContentTestRule,
            "Deleted passkeys for id: $passkeyId",
            false
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun deletePasskeys_multipleUser() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        val users = arrayOf(DEFAULT_USERNAME, DEFAULT_USERNAME_1, DEFAULT_USERNAME_2)

        users.forEach {
            EmbeddedSdkPage.recoverPasskey(composeContentTestRule, it)
            EmbeddedSdkPage.verifyRecoverResult(
                composeContentTestRule,
                "username = $it",
                true
            )
        }

        EmbeddedSdkPage.navigateToManagePasskeys(composeContentTestRule)

        users.forEach {
            val passkeyId = EmbeddedSdkTestUtils.getIdByUsername(
                composeContentTestRule,
                it
            )

            EmbeddedSdkPage.deletePasskey(
                composeContentTestRule,
                passkeyId
            )
            EmbeddedSdkPage.verifyDeletePasskeyResult(
                composeContentTestRule,
                "Deleted passkeys for id: $passkeyId",
                false
            )
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun authenticate_noUser() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.navigateToAuthenticate(composeContentTestRule)

        // Have to use a try/catch as Compose throws an IllegalStateException
        // when the WebView shows as your view disappeared on it
        // "No compose views found in the app. Is your Activity resumed?"
        try {
            EmbeddedSdkPage.authenticatePasskey(composeContentTestRule)
        } catch (e: IllegalStateException) {
            EmbeddedSdkPage.verifyAuthenticateResult(
                composeContentTestRule,
                "MissingCredentialInRealm",
                true
            )
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun authenticate_oneUser() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        recoverPasskey()

        EmbeddedSdkPage.navigateToAuthenticate(composeContentTestRule)

        // Have to use a try/catch as Compose throws an IllegalStateException
        // when the WebView shows as your view disappeared on it
        // "No compose views found in the app. Is your Activity resumed?"
        try {
            EmbeddedSdkPage.authenticatePasskey(composeContentTestRule)
        } catch (e: IllegalStateException) {
            EmbeddedSdkPage.verifyAuthenticateResult(
                composeContentTestRule,
                "acme://?code=",
                true
            )
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun authenticate_multipleUser() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        composeContentTestRule.waitUntil(1_000_000L) {
            val users = arrayOf(DEFAULT_USERNAME, DEFAULT_USERNAME_1, DEFAULT_USERNAME_2)

            users.forEach {
                EmbeddedSdkPage.recoverPasskey(composeContentTestRule, it)
                EmbeddedSdkPage.verifyRecoverResult(
                    composeContentTestRule,
                    "username = $it",
                    true
                )
            }

            EmbeddedSdkPage.navigateToAuthenticate(composeContentTestRule)

            users.forEach {
                val passkeyDisplayName = EmbeddedSdkTestUtils.getDisplayNameByUsername(
                    composeContentTestRule,
                    it
                )

                // Have to use a try/catch as Compose throws an IllegalStateException
                // when the WebView shows as your view disappeared on it
                // "No compose views found in the app. Is your Activity resumed?"
                try {
                    EmbeddedSdkPage.authenticatePasskey(composeContentTestRule, passkeyDisplayName)
                } catch (e: IllegalStateException) {
                    EmbeddedSdkPage.verifyAuthenticateResult(
                        composeContentTestRule,
                        "acme://?code=",
                        true
                    )
                }
            }

            return@waitUntil true
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun verifyURL_emptyBindPasskeyURL() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.navigateToUrlValidation(composeContentTestRule)
        EmbeddedSdkPage.navigateToBindPasskeyUrlButton(composeContentTestRule)
        EmbeddedSdkPage.verifyViewBindPasskeyUrlResult(
            composeContentTestRule,
            "Please provide a Bind Passkey URL",
            false
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun verifyURL_garbageBindPasskeyURL() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.navigateToUrlValidation(composeContentTestRule)
        EmbeddedSdkPage.typeBindPasskeyUrl(composeContentTestRule, GARBAGE_URL)
        EmbeddedSdkPage.navigateToBindPasskeyUrlButton(composeContentTestRule)
        EmbeddedSdkPage.verifyViewBindPasskeyUrlResult(
            composeContentTestRule,
            "false",
            false
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun verifyURL_bindPasskeyURL() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.navigateToUrlValidation(composeContentTestRule)
        EmbeddedSdkPage.typeBindPasskeyUrl(composeContentTestRule, BIND_PASSKEY_URL)
        EmbeddedSdkPage.navigateToBindPasskeyUrlButton(composeContentTestRule)
        EmbeddedSdkPage.verifyViewBindPasskeyUrlResult(
            composeContentTestRule,
            "true",
            false
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun verifyURL_emptyAuthenticateURL() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.navigateToUrlValidation(composeContentTestRule)
        EmbeddedSdkPage.navigateToAuthenticateUrlButton(composeContentTestRule)
        EmbeddedSdkPage.verifyViewAuthenticateUrlResult(
            composeContentTestRule,
            "Please provide an Authenticate URL",
            false
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun verifyURL_garbageAuthenticateURL() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.navigateToUrlValidation(composeContentTestRule)
        EmbeddedSdkPage.typeAuthenticateUrl(composeContentTestRule, GARBAGE_URL)
        EmbeddedSdkPage.navigateToAuthenticateUrlButton(composeContentTestRule)
        EmbeddedSdkPage.verifyViewAuthenticateUrlResult(
            composeContentTestRule,
            "false",
            false
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun verifyURL_authenticateURL() {
        if (shouldSkipTest(object {}.javaClass.enclosingMethod?.name)) return

        EmbeddedSdkPage.navigateToUrlValidation(composeContentTestRule)
        EmbeddedSdkPage.typeAuthenticateUrl(composeContentTestRule, AUTHENTICATE_URL)
        EmbeddedSdkPage.navigateToAuthenticateUrlButton(composeContentTestRule)
        EmbeddedSdkPage.verifyViewAuthenticateUrlResult(
            composeContentTestRule,
            "true",
            false
        )
    }

    private fun shouldSkipTest(testName: String?): Boolean {
        var shouldSkipTest = false

        try {
            val properties = Properties()
            properties.load(InstrumentationRegistry.getInstrumentation().context.assets.open("env.properties"))

            val envName = "skip_$testName"
            val env = properties.getProperty(envName)
            Timber.d("Env Var $envName: $env")

            shouldSkipTest = env != null
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        } finally {
            Timber.d("Test $testName: $shouldSkipTest")
        }

        return shouldSkipTest
    }
}
