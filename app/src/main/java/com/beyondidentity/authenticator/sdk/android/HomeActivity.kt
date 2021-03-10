package com.beyondidentity.authenticator.sdk.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        
    }

    private fun getSession(intent: Intent) =
        intent.data?.getQueryParameter("session")?.let {
            Log.d("Home", "We got our sessions $it")
            it
        } ?: {
            Log.d("Home", "Error signing in")
            null
        }
}