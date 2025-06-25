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

    private const val VIEW_EMBEDDED_SDK = "View Embedded SDK"
    private const val EMBEDDED_SDK_HEADER = "Embedded SDK Header"
    private const val REGISTER_PASSKEY_BUTTON = "Register Passkey Button"
    private const val REGISTER_PASSKEY_INPUT = "Register Passkey Input"
    private const val REGISTER_PASSKEY_RESULT = "Register Passkey Result"
    private const val RECOVER_PASSKEY_BUTTON = "Recover Passkey Button"
    private const val RECOVER_PASSKEY_INPUT = "Recover Passkey Input"
    private const val RECOVER_PASSKEY_RESULT = "Recover Passkey Result"
    private const val MANAGE_PASSKEY_BUTTON = "Manage Passkeys"
    private const val PASSKEY_MANAGEMENT_HEADER = "Passkey Management Header"
    private const val VIEW_PASSKEY_BUTTON = "View Passkey Button"
    private const val VIEW_PASSKEY_RESULT = "View Passkey Result"
    private const val DELETE_PASSKEY_BUTTON = "Delete Passkey Button"
    private const val DELETE_PASSKEY_INPUT = "Delete Passkey Input"
    private const val DELETE_PASSKEY_RESULT = "Delete Passkey Result"
    private const val AUTHENTICATE_BUTTON = "Authenticate"
    private const val AUTHENTICATE_HEADER = "Authenticate Header"
    private const val AUTHENTICATE_WITH_BI_BUTTON = "Authenticate with Beyond Identity Button"
    private const val AUTHENTICATE_WITH_BI_RESULT = "Authenticate with Beyond Identity Result"
    private const val AUTHENTICATE_WITH_OKTA_BUTTON = "Authenticate with Okta Web Button"
    private const val AUTHENTICATE_WITH_OKTA_RESULT = "Authenticate with Okta Web Result"
    private const val AUTHENTICATE_WITH_AUTH0_BUTTON = "Authenticate with Auth0 Web Button"
    private const val AUTHENTICATE_WITH_AUTH0_RESULT = "Authenticate with Auth0 Web Result"
    private const val URL_VALIDATION_BUTTON = "URL Validation"
    private const val URL_VALIDATION_HEADER = "URL Validation Header"
    private const val VALIDATE_BIND_PASSKEY_URL_BUTTON = "Validate Bind Passkey URL Button"
    private const val VALIDATE_BIND_PASSKEY_URL_INPUT = "Validate Bind Passkey URL Input"
    private const val VALIDATE_BIND_PASSKEY_URL_RESULT = "Validate Bind Passkey URL Result"
    private const val VALIDATE_AUTHENTICATE_URL_BUTTON = "Validate Authenticate URL Button"
    private const val VALIDATE_AUTHENTICATE_URL_INPUT = "Validate Authenticate URL Input"
    private const val VALIDATE_AUTHENTICATE_URL_RESULT = "Validate Authenticate URL Result"

    fun navigateToEmbeddedSdk(rule: ComposeContentTestRule) {
        Timber.d("navigateToEmbeddedSdk()")
        rule.onNodeWithText(
            text = VIEW_EMBEDDED_SDK,
            substring = false,
            ignoreCase = false,
            useUnmergedTree = false
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = EMBEDDED_SDK_HEADER,
            useUnmergedTree = false
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
            testTag = REGISTER_PASSKEY_BUTTON,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = REGISTER_PASSKEY_INPUT,
            useUnmergedTree = false
        ).performTextReplacement(username)
        rule.onNodeWithTag(
            testTag = REGISTER_PASSKEY_BUTTON,
            useUnmergedTree = false
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = REGISTER_PASSKEY_RESULT,
            useUnmergedTree = false
        )

        return username
    }

    fun verifyRegisterResult(rule: ComposeContentTestRule, expectedText: String, substring: Boolean = true) {
        Timber.d("verifyRegisterResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = REGISTER_PASSKEY_RESULT,
            text = expectedText,
            substring = substring
        )
    }

    /**
     * Used to recover a passkey
     * @param rule The rule to execute steps against
     * @param username The username to recover. Defaults to jetpackComposeTest<timeMillis>
     */
    fun recoverPasskey(rule: ComposeContentTestRule, username: String) {
        Timber.d("recoverPasskey($username)")
        rule.onNodeWithTag(
            testTag = RECOVER_PASSKEY_BUTTON,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = RECOVER_PASSKEY_INPUT,
            useUnmergedTree = false
        ).performTextReplacement(username)
        rule.onNodeWithTag(
            testTag = RECOVER_PASSKEY_BUTTON,
            useUnmergedTree = false
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = RECOVER_PASSKEY_RESULT,
            useUnmergedTree = false
        )
    }

    fun verifyRecoverResult(rule: ComposeContentTestRule, expectedText: String, substring: Boolean = true) {
        Timber.d("verifyRecoverResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = RECOVER_PASSKEY_RESULT,
            text = expectedText,
            substring = substring
        )
    }

    fun navigateToManagePasskeys(rule: ComposeContentTestRule) {
        Timber.d("navigateToManagePasskeys()")
        rule.onNodeWithTag(
            testTag = MANAGE_PASSKEY_BUTTON,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = MANAGE_PASSKEY_BUTTON,
            useUnmergedTree = false
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = PASSKEY_MANAGEMENT_HEADER,
            useUnmergedTree = false
        )
    }

    fun viewPasskeysInsideManage(rule: ComposeContentTestRule) {
        Timber.d("viewPasskeysInsideManage()")
        rule.onNodeWithTag(
            testTag = VIEW_PASSKEY_BUTTON,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = VIEW_PASSKEY_BUTTON,
            useUnmergedTree = false
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = VIEW_PASSKEY_RESULT,
            useUnmergedTree = false
        )
    }

    fun verifyViewPasskeyResult(rule: ComposeContentTestRule, expectedText: String, substring: Boolean = true) {
        Timber.d("verifyViewPasskeyResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = VIEW_PASSKEY_RESULT,
            text = expectedText,
            substring = substring
        )
    }

    /**
     * Used to delete a passkey
     * @param rule The rule to execute steps against
     * @param passkeyId The id of the passkey to delete
     */
    fun deletePasskey(rule: ComposeContentTestRule, passkeyId: String) {
        Timber.d("deletePasskey($passkeyId)")
        rule.onNodeWithTag(
            testTag = DELETE_PASSKEY_BUTTON,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = DELETE_PASSKEY_INPUT,
            useUnmergedTree = false
        ).performTextReplacement(passkeyId)
        rule.onNodeWithTag(
            testTag = DELETE_PASSKEY_BUTTON,
            useUnmergedTree = false
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = DELETE_PASSKEY_RESULT,
            useUnmergedTree = false
        )
    }

    fun verifyDeletePasskeyResult(rule: ComposeContentTestRule, expectedText: String, substring: Boolean = true) {
        Timber.d("verifyDeletePasskeyResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = DELETE_PASSKEY_RESULT,
            text = expectedText,
            substring = substring
        )
    }

    fun navigateToAuthenticate(rule: ComposeContentTestRule) {
        Timber.d("navigateToAuthenticate()")
        rule.onNodeWithTag(
            testTag = AUTHENTICATE_BUTTON,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = AUTHENTICATE_BUTTON,
            useUnmergedTree = false
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = AUTHENTICATE_HEADER,
            useUnmergedTree = false
        )
    }

    /**
     * Used to authenticate a passkey
     * @param rule The rule to execute steps against
     */
    fun authenticatePasskey(rule: ComposeContentTestRule, text: String? = null) {
        Timber.d("authenticatePasskey($text)")
        rule.onNodeWithTag(
            testTag = AUTHENTICATE_WITH_BI_BUTTON,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = AUTHENTICATE_WITH_BI_BUTTON,
            useUnmergedTree = false
        ).performClick()

        if (text != null) {
            rule.waitForIdle()

            Thread.sleep(rule.DEFAULT_TIMEOUT_MILLIS)

            onView(
                withText(text)
            ).check(
                matches(isDisplayed())
            ).perform(
                click()
            )
        }

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = AUTHENTICATE_WITH_BI_RESULT,
            useUnmergedTree = false
        )
    }

    fun verifyAuthenticateResult(rule: ComposeContentTestRule, expectedText: String, substring: Boolean = true) {
        Timber.d("verifyAuthenticateResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = AUTHENTICATE_WITH_BI_RESULT,
            text = expectedText,
            substring = substring
        )
    }

    fun authenticateOkta(rule: ComposeContentTestRule, text: String? = null) {
        Timber.d("authenticateOkta($text)")
        rule.onNodeWithTag(
            testTag = AUTHENTICATE_WITH_OKTA_BUTTON,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = AUTHENTICATE_WITH_OKTA_BUTTON,
            useUnmergedTree = false
        ).performClick()

        if (text != null) {
            rule.waitForIdle()

            Thread.sleep(rule.DEFAULT_TIMEOUT_MILLIS)

            onView(
                withText(text)
            ).check(
                matches(isDisplayed())
            ).perform(
                click()
            )
        }

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = AUTHENTICATE_WITH_OKTA_RESULT,
            useUnmergedTree = false
        )
    }

    fun verifyAuthenticateOktaResult(rule: ComposeContentTestRule, expectedText: String, substring: Boolean = true) {
        Timber.d("verifyAuthenticateOktaResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = AUTHENTICATE_WITH_OKTA_RESULT,
            text = expectedText,
            substring = substring
        )
    }

    fun authenticateAuth0(rule: ComposeContentTestRule, text: String? = null) {
        Timber.d("authenticateAuth0($text)")
        rule.onNodeWithTag(
            testTag = AUTHENTICATE_WITH_AUTH0_BUTTON,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = AUTHENTICATE_WITH_AUTH0_BUTTON,
            useUnmergedTree = false
        ).performClick()

        if (text != null) {
            rule.waitForIdle()

            Thread.sleep(rule.DEFAULT_TIMEOUT_MILLIS)

            onView(
                withText(text)
            ).check(
                matches(isDisplayed())
            ).perform(
                click()
            )
        }

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = AUTHENTICATE_WITH_AUTH0_RESULT,
            useUnmergedTree = false
        )
    }

    fun verifyAuthenticateAuth0Result(rule: ComposeContentTestRule, expectedText: String, substring: Boolean = true) {
        Timber.d("verifyAuthenticateAuth0Result($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = AUTHENTICATE_WITH_AUTH0_RESULT,
            text = expectedText,
            substring = substring
        )
    }

    fun navigateToUrlValidation(rule: ComposeContentTestRule) {
        Timber.d("navigateToUrlValidation()")
        rule.onNodeWithTag(
            testTag = URL_VALIDATION_BUTTON,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = URL_VALIDATION_BUTTON,
            useUnmergedTree = false
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = URL_VALIDATION_HEADER,
            useUnmergedTree = false
        )
    }

    fun navigateToBindPasskeyUrlButton(rule: ComposeContentTestRule) {
        Timber.d("navigateToBindPasskeyUrlButton()")
        rule.onNodeWithTag(
            testTag = VALIDATE_BIND_PASSKEY_URL_BUTTON,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = VALIDATE_BIND_PASSKEY_URL_BUTTON,
            useUnmergedTree = false
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = VALIDATE_BIND_PASSKEY_URL_BUTTON,
            useUnmergedTree = false
        )
    }

    fun typeBindPasskeyUrl(rule: ComposeContentTestRule, URL: String) {
        Timber.d("typeBindPasskeyUrl($URL)")
        rule.onNodeWithTag(
            testTag = VALIDATE_BIND_PASSKEY_URL_INPUT,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = VALIDATE_BIND_PASSKEY_URL_INPUT,
            useUnmergedTree = false
        ).performTextReplacement(URL)
    }

    fun verifyViewBindPasskeyUrlResult(rule: ComposeContentTestRule, expectedText: String, substring: Boolean = true) {
        Timber.d("verifyViewBindPasskeyUrlResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = VALIDATE_BIND_PASSKEY_URL_RESULT,
            text = expectedText,
            substring = substring
        )
    }

    fun navigateToAuthenticateUrlButton(rule: ComposeContentTestRule) {
        Timber.d("navigateToAuthenticateUrlButton()")
        rule.onNodeWithTag(
            testTag = VALIDATE_AUTHENTICATE_URL_BUTTON,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = VALIDATE_AUTHENTICATE_URL_BUTTON,
            useUnmergedTree = false
        ).performClick()

        rule.waitForIdle()
        rule.waitUntilNodeWithTagExists(
            testTag = VALIDATE_AUTHENTICATE_URL_BUTTON,
            useUnmergedTree = false
        )
    }

    fun typeAuthenticateUrl(rule: ComposeContentTestRule, URL: String) {
        Timber.d("typeAuthenticateUrl($URL)")
        rule.onNodeWithTag(
            testTag = VALIDATE_AUTHENTICATE_URL_INPUT,
            useUnmergedTree = false
        ).performScrollTo()
        rule.onNodeWithTag(
            testTag = VALIDATE_AUTHENTICATE_URL_INPUT,
            useUnmergedTree = false
        ).performTextReplacement(URL)
    }

    fun verifyViewAuthenticateUrlResult(rule: ComposeContentTestRule, expectedText: String, substring: Boolean = true) {
        Timber.d("verifyViewAuthenticateUrlResult($expectedText, $substring)")
        rule.assertNodeWithTextExists(
            testTag = VALIDATE_AUTHENTICATE_URL_RESULT,
            text = expectedText,
            substring = substring
        )
    }
}
