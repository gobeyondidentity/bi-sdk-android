package com.beyondidentity.embedded.sdk.utils

import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [30])
@RunWith(RobolectricTestRunner::class)
class IntentUtilsTest {
    @Test
    fun `supportIntent assert support email intent`() {
        val i = supportIntent("user@gmail.com")

        assertEquals(i.data.toString(), "mailto:user@gmail.com")
        assertEquals(i.action, Intent.ACTION_SENDTO)
    }

    @Test
    fun `supportIntent assert support link intent`() {
        val i = supportIntent("https://beyondidentity.com/support")

        assertEquals(i.data.toString(), "https://beyondidentity.com/support")
        assertEquals(i.action, Intent.ACTION_VIEW)
    }
}
