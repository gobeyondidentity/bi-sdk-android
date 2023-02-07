package com.beyondidentity.authenticator.sdk.android

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.app.KeyguardManager
import android.content.Context
import android.os.Bundle
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import timber.log.Timber

class App : Application(), ActivityLifecycleCallbacks {
    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        EmbeddedSdk.init(
            app = this,
            keyguardPrompt = keyguardPrompt,
            logger = { Timber.d(it) },
        )
        registerActivityLifecycleCallbacks(this)
    }

    @Suppress("DEPRECATION")
    private val keyguardPrompt: ((Boolean, Exception) -> Unit) -> Unit = { answer ->
        (applicationContext?.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager)
            ?.createConfirmDeviceCredentialIntent("Check", "Enter your pin or password")
            ?.let { intent ->
                currentActivity?.startActivityForResult(intent, EMBEDDED_KEYGUARD_REQUEST)
            } ?: answer(false, IllegalStateException("Can not authenticate with KeyGuard!"))
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Timber.d("Setting current activity to ${activity.localClassName}")
        currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        Timber.d("Remove current activity ${activity.localClassName}")
        currentActivity = null
    }

    companion object {
        const val EMBEDDED_KEYGUARD_REQUEST = 9481
    }
}
