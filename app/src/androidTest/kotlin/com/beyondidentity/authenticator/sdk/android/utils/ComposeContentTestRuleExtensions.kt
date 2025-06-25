@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER", "unused", "UnusedReceiverParameter")

package com.beyondidentity.authenticator.sdk.android.utils

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToLog
import timber.log.Timber

val ComposeContentTestRule.DEFAULT_TIMEOUT_MILLIS: Long
    get() = 10_000L

fun ComposeContentTestRule.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS
) {
    Timber.d("waitUntilNodeCount($count, $timeoutMillis)")
    try {
        this.waitUntil(timeoutMillis) {
            this.onAllNodes(matcher).fetchSemanticsNodes().size == count
        }
    } catch (throwable: Throwable) {
        throw catchThrowable(
            tag = "waitUntilNodeCount",
            throwable = throwable
        )
    }
}

@OptIn(ExperimentalTestApi::class)
fun ComposeContentTestRule.waitUntilExists(matcher: SemanticsMatcher, timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS) {
    Timber.d("waitUntilExists($timeoutMillis)")
    try {
        return this.waitUntilNodeCount(matcher, 1, timeoutMillis)
    } catch (throwable: Throwable) {
        throw catchThrowable(
            tag = "waitUntilExists",
            throwable = throwable
        )
    }
}

@OptIn(ExperimentalTestApi::class)
fun ComposeContentTestRule.waitUntilDoesNotExist(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS
) {
    Timber.d("waitUntilDoesNotExist($timeoutMillis)")
    try {
        return this.waitUntilNodeCount(matcher, 0, timeoutMillis)
    } catch (throwable: Throwable) {
        throw catchThrowable(
            tag = "waitUntilDoesNotExist",
            throwable = throwable
        )
    }
}

fun ComposeContentTestRule.waitUntilNodeWithTagExists(testTag: String, useUnmergedTree: Boolean = false) {
    Timber.d("waitUntilNodeWithTagExists($testTag, $useUnmergedTree)")
    try {
        this.waitUntilExists(
            matcher = hasTestTag(testTag),
            timeoutMillis = DEFAULT_TIMEOUT_MILLIS
        )
        this.onNodeWithTag(
            testTag = testTag,
            useUnmergedTree = useUnmergedTree
        ).performScrollTo()
    } catch (throwable: Throwable) {
        throw catchThrowable(
            tag = "waitUntilNodeWithTagExists",
            throwable = throwable
        )
    }
}

fun ComposeContentTestRule.waitUntilNodeWithTextExists(
    text: String,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
    useUnmergedTree: Boolean = false
) {
    Timber.d("waitUntilNodeWithTextExists($text, $substring, $ignoreCase, $useUnmergedTree)")
    try {
        this.waitUntilExists(
            matcher = hasText(text),
            timeoutMillis = DEFAULT_TIMEOUT_MILLIS
        )
        this.onNodeWithText(
            text = text,
            substring = substring,
            ignoreCase = ignoreCase,
            useUnmergedTree = useUnmergedTree
        ).performScrollTo()
    } catch (throwable: Throwable) {
        throw catchThrowable(
            tag = "waitUntilNodeWithTextExists",
            throwable = throwable
        )
    }
}

fun ComposeContentTestRule.assertNodeWithTagContainsText(
    testTag: String,
    value: String,
    useUnmergedTree: Boolean = false
) {
    Timber.d("assertNodeWithTagContainsText($testTag, $value, $useUnmergedTree)")
    try {
        this.waitUntilNodeWithTagExists(
            testTag = testTag,
            useUnmergedTree = useUnmergedTree
        )
        this.onNodeWithTag(
            testTag = testTag,
            useUnmergedTree = useUnmergedTree
        ).assertTextContains(value)
    } catch (throwable: Throwable) {
        throw catchThrowable(
            tag = "assertNodeWithTagContainsText",
            throwable = throwable
        )
    }
}

fun ComposeContentTestRule.assertNodeWithTagEqualsText(
    testTag: String,
    value: String,
    useUnmergedTree: Boolean = false
) {
    Timber.d("assertNodeWithTagEqualsText($testTag, $value, $useUnmergedTree)")
    try {
        this.waitUntilNodeWithTagExists(
            testTag = testTag,
            useUnmergedTree = useUnmergedTree
        )
        this.onNodeWithTag(
            testTag = testTag,
            useUnmergedTree = useUnmergedTree
        ).assertTextEquals(value)
    } catch (throwable: Throwable) {
        throw catchThrowable(
            tag = "assertNodeWithTagEqualsText",
            throwable = throwable
        )
    }
}

fun ComposeContentTestRule.assertNodeWithTextDoesNotExist(
    testTag: String,
    text: String,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
    useUnmergedTree: Boolean = false
) {
    Timber.d("assertNodeWithTextDoesNotExist($testTag, $text, $substring, $ignoreCase, $useUnmergedTree)")
    try {
        this.waitUntilNodeWithTagExists(
            testTag = testTag,
            useUnmergedTree = useUnmergedTree
        )
        this.onNodeWithText(
            text = text,
            substring = substring,
            ignoreCase = ignoreCase,
            useUnmergedTree = useUnmergedTree
        ).assertDoesNotExist()
    } catch (throwable: Throwable) {
        throw catchThrowable(
            tag = "assertNodeWithTextDoesNotExist",
            throwable = throwable
        )
    }
}

fun ComposeContentTestRule.assertNodeWithTextExists(
    testTag: String,
    text: String,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
    useUnmergedTree: Boolean = false
) {
    Timber.d("assertNodeWithTextExists($testTag, $text, $substring, $ignoreCase, $useUnmergedTree)")
    try {
        this.waitUntilNodeWithTagExists(
            testTag = testTag,
            useUnmergedTree = useUnmergedTree
        )
        this.onNodeWithText(
            text = text,
            substring = substring,
            ignoreCase = ignoreCase,
            useUnmergedTree = useUnmergedTree
        ).assertExists()
    } catch (throwable: Throwable) {
        throw catchThrowable(
            tag = "assertNodeWithTextExists",
            throwable = throwable
        )
    }
}

private fun ComposeContentTestRule.catchThrowable(
    useUnmergedTree: Boolean = false,
    tag: String,
    maxDepth: Int = Int.MAX_VALUE,
    throwable: Throwable
): Throwable {
    this.onRoot(
        useUnmergedTree = useUnmergedTree
    ).printToLog(
        tag = tag,
        maxDepth = maxDepth
    )
    return throwable
}
