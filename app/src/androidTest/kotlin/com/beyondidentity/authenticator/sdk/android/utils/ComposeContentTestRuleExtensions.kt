@file:Suppress("unused")

package com.beyondidentity.authenticator.sdk.android.utils

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo

fun ComposeContentTestRule.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long = 1_000L,
) {
    this.waitUntil(timeoutMillis) {
        this.onAllNodes(matcher).fetchSemanticsNodes().size == count
    }
}

fun ComposeContentTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 1_000L,
) {
    return this.waitUntilNodeCount(matcher, 1, timeoutMillis)
}

fun ComposeContentTestRule.waitUntilDoesNotExist(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 1_000L,
) {
    return this.waitUntilNodeCount(matcher, 0, timeoutMillis)
}

fun ComposeContentTestRule.waitUntilNodeWithTagExists(
    testTag: String,
    useUnmergedTree: Boolean = false,
) {
    this.waitUntilExists(
        matcher = hasTestTag(testTag),
        timeoutMillis = 10_000L,
    )
    this.onNodeWithTag(
        testTag = testTag,
        useUnmergedTree = useUnmergedTree,
    ).performScrollTo()
}

fun ComposeContentTestRule.waitUntilNodeWithTextExists(
    text: String,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
    useUnmergedTree: Boolean = false,
) {
    this.waitUntilExists(
        matcher = hasText(text),
        timeoutMillis = 10_000L,
    )
    this.onNodeWithText(
        text = text,
        substring = substring,
        ignoreCase = ignoreCase,
        useUnmergedTree = useUnmergedTree,
    ).performScrollTo()
}

fun ComposeContentTestRule.assertNodeWithTagContainsText(
    testTag: String,
    value: String,
    useUnmergedTree: Boolean = false,
) {
    this.waitUntilNodeWithTagExists(
        testTag = testTag,
    )
    this.onNodeWithTag(
        testTag = testTag,
        useUnmergedTree = useUnmergedTree,
    ).assertTextContains(value)
}

fun ComposeContentTestRule.assertNodeWithTagEqualsText(
    testTag: String,
    value: String,
    useUnmergedTree: Boolean = false,
) {
    this.waitUntilNodeWithTagExists(
        testTag = testTag,
    )
    this.onNodeWithTag(
        testTag = testTag,
        useUnmergedTree = useUnmergedTree,
    ).assertTextEquals(value)
}

fun ComposeContentTestRule.assertNodeWithTextDoesNotExist(
    testTag: String,
    text: String,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
    useUnmergedTree: Boolean = false,
) {
    this.waitUntilNodeWithTagExists(
        testTag = testTag,
    )
    this.onNodeWithText(
        text = text,
        substring = substring,
        ignoreCase = ignoreCase,
        useUnmergedTree = useUnmergedTree,
    ).assertDoesNotExist()
}

fun ComposeContentTestRule.assertNodeWithTextExists(
    testTag: String,
    text: String,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
    useUnmergedTree: Boolean = false,
) {
    this.waitUntilNodeWithTagExists(
        testTag = testTag,
    )
    this.onNodeWithText(
        text = text,
        substring = substring,
        ignoreCase = ignoreCase,
        useUnmergedTree = useUnmergedTree,
    ).assertExists()
}
