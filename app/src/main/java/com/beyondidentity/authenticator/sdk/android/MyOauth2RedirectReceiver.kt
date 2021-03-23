package com.beyondidentity.authenticator.sdk.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.beyondidentity.authenticator.sdk.android.utils.DEFAULT_SHARED_PREFS_KEY
import com.beyondidentity.authenticator.sdk.android.utils.SHARED_PREF_SESSION_KEY

class MyOauth2RedirectReceiver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extractSession(intent)
    }

    // The session is our access token, we can use to make API calls to
    // our acme cloud backend
    private fun extractSession(intent: Intent) {
        lateinit var nextStepIntent: Intent

        intent.data?.getQueryParameter("session")?.let { session ->
            // we got the session, save it and go to home screen to start making network calls
            Log.d("MyOauth2RedirectReceiver", "We got our sessions $session")
            getSharedPreferences(DEFAULT_SHARED_PREFS_KEY, MODE_PRIVATE).edit {
                putString(SHARED_PREF_SESSION_KEY, session)
            }

            nextStepIntent = getNextStepIntent(HomeActivity::class.java)
        } ?: run {
            // there was an error getting the session, go back to sign in screen
            Log.e("MyOauth2RedirectReceiver", "Error getting the session")
            Toast.makeText(this, "Error Signing In", Toast.LENGTH_SHORT).show()
            nextStepIntent = getNextStepIntent(SignInActivity::class.java)
        }

        startActivity(nextStepIntent)
        finish()
    }

    private fun getNextStepIntent(clazz: Class<*>) = Intent(this, clazz).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
}
