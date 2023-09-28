@file:Suppress("unused")

package com.beyondidentity.authenticator.sdk.android.pages

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.beyondidentity.authenticator.sdk.android.utils.DEFAULT_TIMEOUT_MILLIS
import com.beyondidentity.authenticator.sdk.android.utils.assertNodeWithTextExists
import com.beyondidentity.authenticator.sdk.android.utils.waitUntilNodeWithTagExists
import timber.log.Timber

object EmbeddedSdkPage {

    private const val viewEmbeddedSdk = "View Embedded SDK"
    private const val embeddedSdkHeader = "Embedded SDK Header"
    private const val registerPasskeyButton = "Register Passkey Button"
    private const val registerPasskeyInput = "Register Passkey Input"
    private const val registerPasskeyResult = "Register Passkey Result"
    private const val recoverPasskeyButton = "Recover Passkey Button"
    private const val recoverPasskeyInput = "Recover Passkey Input"
    private const val recoverPasskeyResult = "Recover Passkey Result"
    private const val managePasskeyButton = "Manage Passkeys"
    private const val passkeyManagementHeader = "Passkey Management Header"
    private const val viewPasskeyButton = "View Passkey Button"
    private const val viewPasskeyResult = "View Passkey Result"
    private const val deletePasskeyButton = "Delete Passkey Button"
    private const val deletePasskeyInput = "Delete Passkey Input"
    private const val deletePasskeyResult = "Delete Passkey Result"
    private const val authenticateButton = "Authenticate"
    private const val authenticateHeader = "Authenticate Header"
    private const val authenticateWithBIButton = "Authenticate with Beyond Identity Button"
    private const val authenticateWithBIResult = "Authenticate with Beyond Identity Result"
    private const val authenticateWithOktaButton = "Authenticate with Okta Web Button"
    private const val authenticateWithOktaResult = "Authenticate with Okta Web Result"
    private const val authenticateWithAuth0Button = "Authenticate with Auth0 Web Button"
    private const val authenticateWithAuth0Result = "Authenticate with Auth0 Web Result"
    private const val urlValidationButton = "URL Validation"
    private const val urlValidationHeader = "URL Validation Header"
    private const val validateBindPasskeyUrlButton = "Validate Bind Passkey URL Button"
    private const val validateBindPasskeyUrlInput = "Validate Bind Passkey URL Input"
    private const val validateBindPasskeyUrlResult = "Validate Bind Passkey URL Result"
    private const val validateAuthenticateUrlButton = "Validate Authenticate URL Button"
    private const val validateAuthenticateUrlInput = "Validate Authenticate URL Input"
    private const val validateAuthenticateUrlResult = "Validate Authenticate URL Result"

    fun navigateToEmbeddedSdk(
        rule: ComposeContentTestRule,
    ) {
        Timber.d("navigateToEmbeddedSdk()")
        rule.onNodeWithText(
            text = viewEmbeddedSdk,
            substring = false,
            ignoreCase = false,
            useUnmergedTree = false,
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = embeddedSdkHeader,
            useUnmergedTree = false,
        )
    }

    /**
     * Used to register a passkey
     * @param rule The rule to execute steps against
     * @param username The username to register. Defaults to jetpackComposeTest<timeMillis>
     * @return The username that register attempted to register
     */
    fun registerPasskey(
        rule: ComposeContentTestRule,
        username: String = "jetpackComposeTest${System.currentTimeMillis()}"
    ): String {
        Timber.d("registerPasskey($username)")
        rule.onNodeWithTag(
            testTag = registerPasskeyButton,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = registerPasskeyInput,
            useUnmergedTree = false,
        ).performTextReplacement(username)
        rule.onNodeWithTag(
            testTag = registerPasskeyButton,
            useUnmergedTree = false,
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = registerPasskeyResult,
            useUnmergedTree = false,
        )

        return username
    }

    fun verifyRegisterResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        Timber.d("verifyRegisterResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = registerPasskeyResult,
            text = expectedText,
            substring = substring,
        )
    }

    /**
     * Used to recover a passkey
     * @param rule The rule to execute steps against
     * @param username The username to recover. Defaults to jetpackComposeTest<timeMillis>
     */
    fun recoverPasskey(
        rule: ComposeContentTestRule,
        username: String,
    ) {
        Timber.d("recoverPasskey($username)")
        rule.onNodeWithTag(
            testTag = recoverPasskeyButton,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = recoverPasskeyInput,
            useUnmergedTree = false,
        ).performTextReplacement(username)
        rule.onNodeWithTag(
            testTag = recoverPasskeyButton,
            useUnmergedTree = false,
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = recoverPasskeyResult,
            useUnmergedTree = false,
        )
    }

    fun verifyRecoverResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        Timber.d("verifyRecoverResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = recoverPasskeyResult,
            text = expectedText,
            substring = substring,
        )
    }

    fun navigateToManagePasskeys(
        rule: ComposeContentTestRule,
    ) {
        Timber.d("navigateToManagePasskeys()")
        rule.onNodeWithTag(
            testTag = managePasskeyButton,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = managePasskeyButton,
            useUnmergedTree = false,
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = passkeyManagementHeader,
            useUnmergedTree = false,
        )
    }

    fun viewPasskeysInsideManage(
        rule: ComposeContentTestRule,
    ) {
        Timber.d("viewPasskeysInsideManage()")
        rule.onNodeWithTag(
            testTag = viewPasskeyButton,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = viewPasskeyButton,
            useUnmergedTree = false,
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = viewPasskeyResult,
            useUnmergedTree = false,
        )
    }

    fun verifyViewPasskeyResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        Timber.d("verifyViewPasskeyResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = viewPasskeyResult,
            text = expectedText,
            substring = substring,
        )
    }

    /**
     * Used to delete a passkey
     * @param rule The rule to execute steps against
     * @param passkeyId The id of the passkey to delete
     */
    fun deletePasskey(
        rule: ComposeContentTestRule,
        passkeyId: String,
    ) {
        Timber.d("deletePasskey($passkeyId)")
        rule.onNodeWithTag(
            testTag = deletePasskeyButton,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = deletePasskeyInput,
            useUnmergedTree = false,
        ).performTextReplacement(passkeyId)
        rule.onNodeWithTag(
            testTag = deletePasskeyButton,
            useUnmergedTree = false,
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = deletePasskeyResult,
            useUnmergedTree = false,
        )
    }

    fun verifyDeletePasskeyResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        Timber.d("verifyDeletePasskeyResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = deletePasskeyResult,
            text = expectedText,
            substring = substring,
        )
    }

    fun navigateToAuthenticate(
        rule: ComposeContentTestRule,
    ) {
        Timber.d("navigateToAuthenticate()")
        rule.onNodeWithTag(
            testTag = authenticateButton,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = authenticateButton,
            useUnmergedTree = false,
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = authenticateHeader,
            useUnmergedTree = false,
        )
    }

    /**
     * Used to authenticate a passkey
     * @param rule The rule to execute steps against
     */
    fun authenticatePasskey(
        rule: ComposeContentTestRule,
        text: String? = null,
    ) {
        Timber.d("authenticatePasskey($text)")
        rule.onNodeWithTag(
            testTag = authenticateWithBIButton,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = authenticateWithBIButton,
            useUnmergedTree = false,
        ).performClick()

        if (text != null) {
            rule.waitForIdle()

            Thread.sleep(rule.DEFAULT_TIMEOUT_MILLIS)

            onView(
                withText(text),
            ).check(
                matches(isDisplayed()),
            ).perform(
                click(),
            )
        }

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = authenticateWithBIResult,
            useUnmergedTree = false,
        )
    }

    fun verifyAuthenticateResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        Timber.d("verifyAuthenticateResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = authenticateWithBIResult,
            text = expectedText,
            substring = substring,
        )
    }

    fun authenticateOkta(
        rule: ComposeContentTestRule,
        text: String? = null,
    ) {
        Timber.d("authenticateOkta($text)")
        rule.onNodeWithTag(
            testTag = authenticateWithOktaButton,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = authenticateWithOktaButton,
            useUnmergedTree = false,
        ).performClick()

        if (text != null) {
            rule.waitForIdle()

            Thread.sleep(rule.DEFAULT_TIMEOUT_MILLIS)

            onView(
                withText(text),
            ).check(
                matches(isDisplayed()),
            ).perform(
                click(),
            )
        }

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = authenticateWithOktaResult,
            useUnmergedTree = false,
        )
    }

    fun verifyAuthenticateOktaResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        Timber.d("verifyAuthenticateOktaResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = authenticateWithOktaResult,
            text = expectedText,
            substring = substring,
        )
    }

    fun authenticateAuth0(
        rule: ComposeContentTestRule,
        text: String? = null,
    ) {
        Timber.d("authenticateAuth0($text)")
        rule.onNodeWithTag(
            testTag = authenticateWithAuth0Button,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = authenticateWithAuth0Button,
            useUnmergedTree = false,
        ).performClick()

        if (text != null) {
            rule.waitForIdle()

            Thread.sleep(rule.DEFAULT_TIMEOUT_MILLIS)

            onView(
                withText(text),
            ).check(
                matches(isDisplayed()),
            ).perform(
                click(),
            )
        }

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = authenticateWithAuth0Result,
            useUnmergedTree = false,
        )
    }

    fun verifyAuthenticateAuth0Result(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        Timber.d("verifyAuthenticateAuth0Result($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = authenticateWithAuth0Result,
            text = expectedText,
            substring = substring,
        )
    }

    fun navigateToUrlValidation(
        rule: ComposeContentTestRule,
    ) {
        Timber.d("navigateToUrlValidation()")
        rule.onNodeWithTag(
            testTag = urlValidationButton,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = urlValidationButton,
            useUnmergedTree = false,
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = urlValidationHeader,
            useUnmergedTree = false,
        )
    }

    fun navigateToBindPasskeyUrlButton(
        rule: ComposeContentTestRule,
    ) {
        Timber.d("navigateToBindPasskeyUrlButton()")
        rule.onNodeWithTag(
            testTag = validateBindPasskeyUrlButton,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = validateBindPasskeyUrlButton,
            useUnmergedTree = false,
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = validateBindPasskeyUrlButton,
            useUnmergedTree = false,
        )
    }

    fun typeBindPasskeyUrl(
        rule: ComposeContentTestRule,
        URL: String,
    ) {
        Timber.d("typeBindPasskeyUrl($URL)")
        rule.onNodeWithTag(
            testTag = validateBindPasskeyUrlInput,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = validateBindPasskeyUrlInput,
            useUnmergedTree = false,
        ).performTextReplacement(URL)
    }

    fun verifyViewBindPasskeyUrlResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        Timber.d("verifyViewBindPasskeyUrlResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = validateBindPasskeyUrlResult,
            text = expectedText,
            substring = substring,
        )
    }

    fun navigateToAuthenticateUrlButton(
        rule: ComposeContentTestRule,
    ) {
        Timber.d("navigateToAuthenticateUrlButton()")
        rule.onNodeWithTag(
            testTag = validateAuthenticateUrlButton,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = validateAuthenticateUrlButton,
            useUnmergedTree = false,
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = validateAuthenticateUrlButton,
            useUnmergedTree = false,
        )
    }

    fun typeAuthenticateUrl(
        rule: ComposeContentTestRule,
        URL: String,
    ) {
        Timber.d("typeAuthenticateUrl($URL)")
        rule.onNodeWithTag(
            testTag = validateAuthenticateUrlInput,
            useUnmergedTree = false,
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = validateAuthenticateUrlInput,
            useUnmergedTree = false,
        ).performTextReplacement(URL)
    }

    fun verifyViewAuthenticateUrlResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        Timber.d("verifyViewAuthenticateUrlResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = validateAuthenticateUrlResult,
            text = expectedText,
            substring = substring,
        )
    }
}
