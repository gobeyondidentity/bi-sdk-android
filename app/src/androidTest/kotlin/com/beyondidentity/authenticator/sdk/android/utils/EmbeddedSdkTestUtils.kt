package com.beyondidentity.authenticator.sdk.android.utils

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.beyondidentity.embedded.sdk.EmbeddedSdk

class EmbeddedSdkTestUtils {
    companion object {
        @JvmStatic
        fun getIdByUsername(rule: ComposeContentTestRule, username: String): String {
            var id = ""

            rule.waitUntil(rule.DEFAULT_TIMEOUT_MILLIS) {
                EmbeddedSdk.getPasskeys { passkeyListResult ->
                    passkeyListResult.getOrDefault(emptyList()).forEach { passkey ->
                        if (passkey.identity.username == username) {
                            id = passkey.id
                        }
                    }
                }
                id != ""
            }

            return id
        }

        @JvmStatic
        fun getDisplayNameByUsername(rule: ComposeContentTestRule, username: String): String {
            var displayName = ""

            rule.waitUntil(rule.DEFAULT_TIMEOUT_MILLIS) {
                EmbeddedSdk.getPasskeys { passkeyListResult ->
                    passkeyListResult.getOrDefault(emptyList()).forEach { passkey ->
                        if (passkey.identity.username == username) {
                            displayName = passkey.identity.displayName
                        }
                    }
                }
                displayName != ""
            }

            return displayName
        }
    }
}
