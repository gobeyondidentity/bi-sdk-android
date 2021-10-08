package com.beyondidentity.embedded.sdk

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EmbeddedSdkTest {

    @Before
    fun setup() {
        EmbeddedSdk.init(
            app = ApplicationProvider.getApplicationContext(),
            keyguardPrompt = null,
            logger = {},
        )
    }

    @Test
    fun createPkce_callback_success() {
        EmbeddedSdk.createPkce {
            assertTrue(it.isSuccess)
        }
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
}
