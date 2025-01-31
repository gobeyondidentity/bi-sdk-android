package com.beyondidentity.authenticator.sdk.android.embedded.customtab

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import timber.log.Timber

class EmbeddedCustomTabActivity : ComponentActivity() {

    lateinit var customTabsClient: CustomTabsClient
    lateinit var customTabsServiceConnection: CustomTabsServiceConnection
    lateinit var customTabsSession: CustomTabsSession
    var builder = CustomTabsIntent.Builder()

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate(${intent?.dataString})")

        val customTabsCallback = object : CustomTabsCallback() {
            override fun onNavigationEvent(
                navigationEvent: Int,
                extras: Bundle?,
            ) {
                super.onNavigationEvent(navigationEvent, extras)
                Timber.d("onNavigationEvent($navigationEvent, $extras)")

                when (navigationEvent) {
                    NAVIGATION_STARTED -> {}
                    NAVIGATION_FINISHED -> {}
                    NAVIGATION_FAILED -> {}
                    NAVIGATION_ABORTED -> {}
                    TAB_SHOWN -> {}
                    TAB_HIDDEN -> {}
                    else -> {}
                }
            }
        }

        customTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                name: ComponentName,
                client: CustomTabsClient,
            ) {
                Timber.d("onCustomTabsServiceConnected($name, $client)")

                customTabsClient = client
                customTabsClient.warmup(0L)
                customTabsSession = customTabsClient.newSession(customTabsCallback)!!
                builder.setSession(customTabsSession)
            }

            override fun onServiceDisconnected(
                name: ComponentName?,
            ) {
                Timber.d("onServiceDisconnected($name)")
            }
        }

        CustomTabsClient.bindCustomTabsService(
            this@EmbeddedCustomTabActivity,
            "com.android.chrome",
            customTabsServiceConnection,
        )

        // CustomTabsIntent
        builder.setShowTitle(true)

        intent?.data?.let { uri ->
            builder.build().launchUrl(this@EmbeddedCustomTabActivity, uri)
        }
    }

    override fun onNewIntent(
        intent: Intent,
    ) {
        super.onNewIntent(intent)
        Timber.d("onNewIntent(${intent?.dataString})")
    }

    override fun onDestroy() {
        try {
            unbindService(customTabsServiceConnection)
        } catch (e: Exception) {
            Timber.e(e)
        }

        super.onDestroy()
        Timber.d("onDestroy()")
    }
}
