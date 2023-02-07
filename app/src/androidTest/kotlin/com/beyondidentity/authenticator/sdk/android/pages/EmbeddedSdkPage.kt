package com.beyondidentity.authenticator.sdk.android.pages

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import com.beyondidentity.authenticator.sdk.android.utils.assertNodeWithTextExists
import com.beyondidentity.authenticator.sdk.android.utils.waitUntilNodeWithTagExists

object EmbeddedSdkPage {

    private const val registerPasskeyButton = "Register Passkey"
    private const val registerPasskeyInput = "Register Passkey Input"
    private const val registerPasskeyResult = "Register Passkey Result"
    private const val recoverPasskeyButton = "Recover Passkey"
    private const val recoverPasskeyInput = "Recover Passkey Input"
    private const val recoverPasskeyResult = "Recover Passkey Result"
    private const val managePasskeyButton = "Manage Passkeys"
    private const val viewPasskeyButton = "View Passkey"
    private const val viewPasskeyResult = "View Passkey Result"
    private const val deletePasskeyButton = "Delete Passkey"
    private const val deletePasskeyInput = "Delete Passkey Input"
    private const val deletePasskeyResult = "Delete Passkey Result"
    private const val urlValidationButton = "URL Validation"
    private const val passkeyUrlButton = "Validate Bind Passkey URL"
    private const val bindPasskeyUrlResult = "Validate Bind Passkey URL Result"
    private const val authenticateUrlButton = "Validate Authenticate URL"
    private const val authenticateUrlResult = "Validate Authenticate URL Result"
    private const val bindPasskeyUrlInput = "Validate Bind Passkey URL Input"
    private const val authenticateUrlInput = "Validate Authenticate URL Input"
    private const val navToAuthenticateButton = "Authenticate"
    private const val authenticateBIButton = "Authenticate with Beyond Identity"
    private const val authenticateBIResult = "Authenticate with Beyond Identity Result"

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
        rule.onNodeWithTag(registerPasskeyButton)
            .performScrollTo()
        rule.onNodeWithTag(registerPasskeyInput)
            .performTextReplacement(username)
        rule.onNodeWithTag(registerPasskeyButton)
            .performClick()

        rule.waitUntilNodeWithTagExists(registerPasskeyResult)

        return username
    }

    fun verifyRegisterResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        rule.assertNodeWithTextExists(
            testTag = registerPasskeyResult,
            text = expectedText,
            substring = substring,
        )
    }

    fun navigateToAuthenticate(
        rule: ComposeContentTestRule,
    ) {
        rule.onNodeWithTag(navToAuthenticateButton)
            .performScrollTo()
        rule.onNodeWithTag(navToAuthenticateButton)
            .performClick()
    }

    /**
     * Used to authenticate a passkey
     * @param rule The rule to execute steps against
     */
    fun authenticatePasskey(
        rule: ComposeContentTestRule,
    ) {
        rule.onNodeWithTag(authenticateBIButton)
            .performClick()
        rule.waitForIdle()
    }

    fun verifyAuthenticateResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        rule.assertNodeWithTextExists(
            testTag = authenticateBIResult,
            text = expectedText,
            substring = substring,
        )
    }

    /**
     * Used to recover a passkey
     * @param rule The rule to execute steps against
     * @param username The username to recover. Defaults to jetpackComposeTest<timeMillis>
     */
    fun recoverPasskey(rule: ComposeContentTestRule, username: String) {
        rule.onNodeWithTag(recoverPasskeyButton)
            .performScrollTo()
        rule.onNodeWithTag(recoverPasskeyInput)
            .performTextReplacement(username)
        rule.onNodeWithTag(recoverPasskeyButton)
            .performClick()

        rule.waitUntilNodeWithTagExists(recoverPasskeyResult)
    }

    fun verifyRecoverResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        rule.assertNodeWithTextExists(
            testTag = recoverPasskeyResult,
            text = expectedText,
            substring = substring,
        )
    }

    fun navigateToManagePasskeys(rule: ComposeContentTestRule) {
        rule.onNodeWithTag(managePasskeyButton)
            .performScrollTo()
            .performClick()

        rule.waitForIdle()
    }

    fun navigateToUrlValidation(rule: ComposeContentTestRule) {
        rule.onNodeWithTag(urlValidationButton)
            .performScrollTo()
            .performClick()
        rule.waitForIdle()
    }

    fun navigateToBindPassUrlButton(rule: ComposeContentTestRule) {
        rule.onNodeWithTag(passkeyUrlButton)
            .performScrollTo()
            .performClick()
        rule.waitForIdle()
    }

    fun verifyViewBindPassUrlResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        rule.assertNodeWithTextExists(
            testTag = bindPasskeyUrlResult,
            text = expectedText,
            substring = substring,
        )
    }

    fun navigateToAuthenticateUrlButton(rule: ComposeContentTestRule) {
        rule.onNodeWithTag(authenticateUrlButton)
            .performScrollTo()
            .performClick()
        rule.waitForIdle()
    }

    fun verifyViewAuthenticateUrlResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        rule.assertNodeWithTextExists(
            testTag = authenticateUrlResult,
            text = expectedText,
            substring = substring,
        )
    }

    fun typeBindPassUrl(rule: ComposeContentTestRule, URL: String) {
        rule.onNodeWithTag(bindPasskeyUrlInput)
            .performScrollTo()
            .performTextReplacement(URL)
    }

    fun typeAuthenticateUrl(rule: ComposeContentTestRule, URL: String) {
        rule.onNodeWithTag(authenticateUrlInput)
            .performScrollTo()
            .performTextReplacement(URL)
    }

    fun viewPasskeysInsideManage(rule: ComposeContentTestRule) {
        rule.onNodeWithTag(viewPasskeyButton)
            .performScrollTo()
            .performClick()

        rule.waitForIdle()
    }

    fun verifyViewPasskeyResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
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
    fun deletePasskey(rule: ComposeContentTestRule, passkeyId: String) {
        rule.onNodeWithTag(deletePasskeyButton)
            .performScrollTo()
        rule.onNodeWithTag(deletePasskeyInput)
            .performTextReplacement(passkeyId)
        rule.onNodeWithTag(deletePasskeyButton)
            .performClick()

        rule.waitUntilNodeWithTagExists(deletePasskeyResult)
    }

    fun verifyDeletePasskeyResult(
        rule: ComposeContentTestRule,
        expectedText: String,
        substring: Boolean = true
    ) {
        rule.assertNodeWithTextExists(
            testTag = deletePasskeyResult,
            text = expectedText,
            substring = substring,
        )
    }

    fun navigateToEmbeddedSdk(rule: ComposeContentTestRule) {
        rule.onNodeWithText(
            text = "View Embedded SDK",
            substring = false,
            ignoreCase = false,
            useUnmergedTree = false
        ).performClick()
    }
}
